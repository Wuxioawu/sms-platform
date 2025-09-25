package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.constant.SmsConstant;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.StrategyException;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.uitl.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service(value = "limitOneMinute")
@Slf4j
public class LimitOneMinuteStrategyFilter implements StrategyFilter {

    private final String UTC = "+8";

    private final long ONE_MINUTE = 60 * 1000 - 1;

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    @Override
    public void strategy(StandardSubmit submit) {
        // only vertify message have the limitation
        if (submit.getState() != SmsConstant.CODE_TYPE) {
            return;
        }
        log.info("【Strategy Module - One-Minute Rate Limiting】  Starting validation...");

        long sendTimeMilli = submit.getSendTime().toInstant(ZoneOffset.of(UTC)).toEpochMilli();

        String key = CacheConstant.LIMIT_MINUTES + submit.getClientId() + CacheConstant.SEPARATE + submit.getMobile();
        Boolean addOk = cacheClient.zadd(key, sendTimeMilli, sendTimeMilli);

        // If insertion fails, it means concurrent situation: 60s limit reached, cannot send
        if (!addOk) {
            log.info("【Strategy Module - One-Minute Rate Limiting】  Insertion failed! One-minute limit reached, cannot send!");
            sendErrorMsg(submit);
        }

        // 6. Use zrangebyscore to check if only one SMS exists within the last 1 minute
        long start = sendTimeMilli - ONE_MINUTE;
        int count = cacheClient.zRangeByScoreCount(key, Double.parseDouble(start + ""), Double.parseDouble(sendTimeMilli + ""));

        // 7. If there are 2 or more SMS within 1 minute, limit rule triggered: cannot send
        if (count > 1) {
            log.info("【Strategy Module - One-Minute Rate Limiting】  there are 2 or more SMS within 1 minute, limit rule triggered: cannot send!");
            cacheClient.zRemove(key, sendTimeMilli + "");
            sendErrorMsg(submit);
        }
        log.info("【Strategy Module - One-Minute Rate Limiting】  Rule passed, SMS can be sent!");
    }

    private void sendErrorMsg(StandardSubmit submit) {
        submit.setErrorMsg(ExceptionEnums.ONE_MINUTE_LIMIT + ", mobile = " + submit.getMobile());
        errorSendMsgUtil.sendWriteLog(submit);
        errorSendMsgUtil.sendPushReport(submit);
        throw new StrategyException(ExceptionEnums.ONE_MINUTE_LIMIT);
    }
}














