package com.peng.sms.mq;

import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.model.StandardReport;
import com.peng.sms.service.SearchService;
import com.peng.sms.util.SearchUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SmsUpdateLogListener {

    @Autowired
    private SearchService searchService;

    @RabbitListener(queues = {RabbitMQConstants.SMS_GATEWAY_DEAD_QUEUE})
    public void consume(StandardReport report, Channel channel, Message message) throws IOException {
        log.info("[Search Module - Update Log] Received a message to update the log, report = {}", report);

        SearchUtils.set(report);
        Map<String, Object> doc = new HashMap<>();
        doc.put("reportState", report.getReportState());
        searchService.update(SearchUtils.INDEX + SearchUtils.getYear(), report.getSequenceId().toString(), doc);

        // ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
