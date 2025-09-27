package com.peng.sms.service.impl;

import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.SearchException;
import com.peng.sms.model.StandardReport;
import com.peng.sms.service.SearchService;
import com.peng.sms.util.SearchUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ElasticsearchServiceImpl implements SearchService {

    private static final String CREATED = "created";
    private static final String UPDATED = "updated";

    private static final String TEXT_FIELD = "text";
    private static final String SEND_TIME_FIELD = "sendTime";


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void index(String index, String id, String json) throws IOException {
        IndexRequest request = new IndexRequest();
        request.index(index);
        request.id(id);
        request.source(json, XContentType.JSON);

        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        String result = response.getResult().getLowercase();

        if (!CREATED.equals(result)) {
            log.error("[Search Module - Data Insert Failed] index = {}, id = {}, json = {}, result = {}", index, id, json, result);
            throw new SearchException(ExceptionEnums.SEARCH_INDEX_ERROR);
        }
        log.info("[Search Module - Data Insert Success] Document successfully added to index.");
    }

    @Override
    public boolean exists(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }

    @Override
    public void update(String index, String id, Map<String, Object> doc) throws IOException {
        if (!exists(index, id)) {
            handleMissingDocument();
            return;
        }
        // 2. Document exists, proceed with update
        UpdateRequest request = new UpdateRequest(index, id).doc(doc);
        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);

        if (!UPDATED.equals(update.getResult().getLowercase())) {
            log.error("[Search Module - Update Failed] index = {}, id = {}, doc = {}", index, id, doc);
            throw new SearchException(ExceptionEnums.SEARCH_UPDATE_ERROR);
        }
        log.info("[Search Module - Update Success] Document successfully updated. index = {}, id = {}, doc = {}", index, id, doc);
    }

    @Override
    public Map<String, Object> findSmsByParameters(Map<String, Object> parameters) throws IOException {
        // 1. Create SearchRequest
        SearchRequest request = new SearchRequest(SearchUtils.getCurrYearIndex(), "");

        // 2. Extract parameters
        Object fromObj = parameters.get("from");
        Object sizeObj = parameters.get("size");
        Object contentObj = parameters.get("content");
        Object mobileObj = parameters.get("mobile");
        Object startTimeObj = parameters.get("starttime");
        Object stopTimeObj = parameters.get("stoptime");
        Object clientIDObj = parameters.get("clientID");

        // 2.2 Handle clientID parameter
        List<Long> clientIDList = null;
        if (clientIDObj instanceof List) {
            clientIDList = (List<Long>) clientIDObj;
        } else if (!ObjectUtils.isEmpty(clientIDObj)) {
            clientIDList = Collections.singletonList(Long.parseLong(clientIDObj + ""));
        }

        // 2.3 Build search query
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 2.3.1 Keyword search with highlighting
        if (!ObjectUtils.isEmpty(contentObj)) {
            boolQuery.must(QueryBuilders.matchQuery("text", contentObj));
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("text");
            highlightBuilder.preTags("<span style='color: red'>");
            highlightBuilder.postTags("</span>");
            highlightBuilder.fragmentSize(100);
            sourceBuilder.highlighter(highlightBuilder);
        }

        // 2.3.2 Mobile number prefix search
        if (!ObjectUtils.isEmpty(mobileObj)) {
            boolQuery.must(QueryBuilders.prefixQuery("mobile", (String) mobileObj));
        }

        // 2.3.3 Start time
        if (!ObjectUtils.isEmpty(startTimeObj)) {
            boolQuery.must(QueryBuilders.rangeQuery("sendTime").gte(startTimeObj));
        }

        // 2.3.4 End time
        if (!ObjectUtils.isEmpty(stopTimeObj)) {
            boolQuery.must(QueryBuilders.rangeQuery("sendTime").lte(stopTimeObj));
        }

        // 2.3.5 Client IDs
        if (clientIDList != null) {
            boolQuery.must(QueryBuilders.termsQuery("clientId", clientIDList.toArray(new Long[0])));
        }

        // 2.3.6 Pagination
        sourceBuilder.from(Integer.parseInt(fromObj + ""));
        sourceBuilder.size(Integer.parseInt(sizeObj + ""));

        // Apply query
        sourceBuilder.query(boolQuery);
        request.source(sourceBuilder);

        // 3. Execute search
        SearchResponse resp = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        // 4. Process results
        long total = resp.getHits().getTotalHits().value;
        List<Map> rows = new ArrayList<>();
        for (SearchHit hit : resp.getHits().getHits()) {
            Map<String, Object> row = hit.getSourceAsMap();
            List sendTime = (List) row.get("sendTime");
            String sendTimeStr = listToDateString(sendTime);
            row.put("sendTimeStr", sendTimeStr);
            row.put("corpname", row.get("sign"));

            // Apply highlighting if present
            HighlightField highlightField = hit.getHighlightFields().get("text");
            if (highlightField != null) {
                String textHighLight = highlightField.getFragments()[0].toString();
                row.put("text", textHighLight);
            }
            rows.add(row);
        }

        // 5. Return results
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }

    private void handleMissingDocument() {
        StandardReport report = SearchUtils.get();
        if (report.getReUpdate()) {
            // Second attempt to update after a 20s delay
            log.error("[Search Module - Update Failed]-> ElasticsearchServiceImpl -> handleMissingDocument: Update failed after retry: report={}", report);
        } else {
            report.setReUpdate(true);
            rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_GATEWAY_NORMAL_QUEUE, report);
        }
        SearchUtils.remove();
    }

    private String listToDateString(List sendTime) {
        String year = sendTime.get(0) + "";
        Integer monthInt = (Integer) sendTime.get(1);
        Integer dayInt = (Integer) sendTime.get(2);
        Integer hourInt = (Integer) sendTime.get(3);
        Integer minuteInt = (Integer) sendTime.get(4);
        Integer secondInt = (Integer) sendTime.get(5);

        String month = monthInt / 10 == 0 ? "0" + monthInt : monthInt + "";
        String day = dayInt / 10 == 0 ? "0" + dayInt : dayInt + "";
        String hour = hourInt / 10 == 0 ? "0" + hourInt : hourInt + "";
        String minute = minuteInt / 10 == 0 ? "0" + minuteInt : minuteInt + "";
        String second = secondInt / 10 == 0 ? "0" + secondInt : secondInt + "";

        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
}
