package com.peng.sms.service.impl;

import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.SearchException;
import com.peng.sms.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class ElasticsearchServiceImpl implements SearchService {

    private final String CREATED = "created";

    private final String UPDATED = "updated";

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
            // Insert failed
            log.error("【Search Module - Data Insertion Failed】 index = {}, id = {}, json = {}, result = {}", index, id, json, result);
            throw new SearchException(ExceptionEnums.SEARCH_INDEX_ERROR);
        }
        log.info("【Search Module - Data Insertion Success】 Document successfully added to index");
    }

    @Override
    public boolean exists(String index, String id) throws IOException {
        return false;
    }

    @Override
    public void update(String index, String id, Map<String, Object> doc) throws IOException {

    }

    @Override
    public Map<String, Object> findSmsByParameters(Map<String, Object> parameters) throws IOException {
        return Collections.emptyMap();
    }
}
