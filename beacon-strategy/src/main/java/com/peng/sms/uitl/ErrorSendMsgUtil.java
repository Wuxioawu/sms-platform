package com.peng.sms.uitl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.constant.SmsConstant;
import com.peng.sms.model.StandardReport;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class ErrorSendMsgUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
     * Send a write-log message when the strategy module validation fails
     *
     * @param submit the SMS submission object
     */
    public void sendWriteLog(StandardSubmit submit) {
        submit.setReportState(SmsConstant.REPORT_FAIL);
        // Send the message to the write-log queue
        log.info("ErrorSendMsgUtil =-> sendWriteLog " + submit);
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG, submit);
    }

    /**
     * Send a status report when the strategy module validation fails
     */
    public void sendPushReport(StandardSubmit submit) {
        log.info("ErrorSendMsgUtil =-> sendPushReport " + submit);
        // Get the client's isCallback value
        Integer isCallback = cacheClient.hgetInteger(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), "isCallback");
        // Check if a callback to the client is required
        if (isCallback == 1) {
            // If a callback is needed, retrieve the client's callback URL
            String callbackUrl = cacheClient.hget(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), "callbackUrl");
            // Proceed only if the callback URL is not empty
            if (!StringUtils.isEmpty(callbackUrl)) {
                // Prepare the report information to be pushed to the client
                StandardReport report = new StandardReport();
                BeanUtils.copyProperties(submit, report);
                report.setIsCallback(isCallback);
                report.setCallbackUrl(callbackUrl);
                // Send the message to RabbitMQ
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PUSH_REPORT, report);
            }
        }
    }
}
