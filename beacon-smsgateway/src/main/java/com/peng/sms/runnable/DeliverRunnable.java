package com.peng.sms.runnable;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.constant.SmsConstant;
import com.peng.sms.model.StandardReport;
import com.peng.sms.util.CMPP2DeliverUtil;
import com.peng.sms.util.CMPPDeliverMapUtil;
import com.peng.sms.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.StringUtils;

@Slf4j
public class DeliverRunnable implements Runnable {

    private RabbitTemplate rabbitTemplate = SpringUtil.getBeanByClass(RabbitTemplate.class);

    private BeaconCacheClient cacheClient = SpringUtil.getBeanByClass(BeaconCacheClient.class);

    private final String DELIVRD = "DELIVRD";

    private long msgId;

    private String stat;

    public DeliverRunnable(long msgId, String stat) {
        this.msgId = msgId;
        this.stat = stat;
    }

    @Override
    public void run() {

        StandardReport report = CMPPDeliverMapUtil.remove(msgId + "");

        if (!StringUtils.isEmpty(stat) && stat.equals(DELIVRD)) {
            log.info("[SMS Gateway Module] -> DeliverRunnable: send message successfully msgId : " + msgId);
            report.setReportState(SmsConstant.REPORT_SUCCESS);
        } else {
            report.setReportState(SmsConstant.REPORT_FAIL);
            report.setErrorMsg(CMPP2DeliverUtil.getResultMessage(stat));
        }

        Integer isCallback = cacheClient.hgetInteger(CacheConstant.CLIENT_BUSINESS + report.getApikey(), "isCallback");
        if (isCallback == 1) {
            String callbackUrl = cacheClient.hget(CacheConstant.CLIENT_BUSINESS + report.getApikey(), "callbackUrl");
            if (!StringUtils.isEmpty(callbackUrl)) {
                log.info("[SMS Gateway Module] -> DeliverRunnable: the callback url is {}", callbackUrl);
                report.setIsCallback(isCallback);
                report.setCallbackUrl(callbackUrl);
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PUSH_REPORT, report);
            }
        }
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_GATEWAY_NORMAL_EXCHANGE, "", report);
    }
}
