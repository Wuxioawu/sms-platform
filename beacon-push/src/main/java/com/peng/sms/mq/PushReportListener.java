package com.peng.sms.mq;

import com.peng.sms.config.RabbitMQConfig;
import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.model.StandardReport;
import com.peng.sms.util.JsonUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@Slf4j
public class PushReportListener {

    private final int[] delayTime = {0, 15000, 30000, 60000, 300000};

    private final String SUCCESS = "SUCCESS";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Monitor messages pushed from the strategy module (currently for strategy).
    @RabbitListener(queues = RabbitMQConstants.SMS_PUSH_REPORT)
    public void consume(StandardReport report, Channel channel, Message message) throws IOException {
        String callbackUrl = report.getCallbackUrl();

        if (callbackUrl == null || callbackUrl.isEmpty()) {
            log.info("【Push Module - Send Status Report】 The customer has not set a callback URL! callbackUrl = {}", callbackUrl);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        boolean flag = pushReport(report);
        isResend(report, flag);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    //Listen for messages routed from a delayed exchange
    @RabbitListener(queues = RabbitMQConfig.DELAYED_QUEUE)
    public void delayedConsume(StandardReport report, Channel channel, Message message) throws IOException {
        boolean flag = pushReport(report);
        isResend(report, flag);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    private void isResend(StandardReport report, boolean flag) {
        if (flag) {
            log.info("【Push Module - Send Status Report】 First attempt to push status report succeeded! report = {}", report);
        }

        log.info("【Push Module - Send Status Report】->isResend: Attempt #{} to push status report failed! report = {}", report.getResendCount() + 1, report);
        report.setResendCount(report.getResendCount() + 1);

        // Stop retrying after 5 attempts
        if (report.getResendCount() >= 5) {
            return;
        }

        // Send the report to the delayed queue for retry
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DELAYED_EXCHANGE,
                "",
                report,
                message -> {
                    // Set the delay time for retry
                    message.getMessageProperties().setDelay(delayTime[report.getResendCount()]);
                    return message;
                }
        );
    }

    private boolean pushReport(StandardReport report) {
        boolean flag = false;
        String body = JsonUtil.obj2JSON(report);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            log.info("【Push Module - Send Status Report】->pushReport: Attempt #{} to push status report starts! report = {}", report.getResendCount() + 1, report);
            String result = restTemplate.postForObject(
                    "http://" + report.getCallbackUrl(),
                    new HttpEntity<>(body, httpHeaders),
                    String.class
            );
            flag = SUCCESS.equals(result);
        } catch (RestClientException e) {
            // Exception handling (currently empty)
            log.info("【Push Module - Send Status Report】 post http request find RestClientException: " + e.getMessage());
            e.printStackTrace();
        }
        return flag;
    }
}
