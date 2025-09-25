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

@Service(value = "limitOneHour")
@Slf4j
public class LimitOneHourStrategyFilter implements StrategyFilter {

    private final String UTC = "+8";

    private final long ONE_HOUR = 60 * 1000 * 60 - 1;

    private final int RETRY_COUNT = 2;

    private final int LIMIT_HOUR = 3;

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    @Override
    public void strategy(StandardSubmit submit) {
        // just vertify type message need to do limitation
        if (submit.getState() != SmsConstant.CODE_TYPE) {
            return;
        }

        long sendTimeMilli = submit.getSendTime().toInstant(ZoneOffset.of(UTC)).toEpochMilli();
        submit.setOneHourLimitMilli(sendTimeMilli);

        String key = CacheConstant.LIMIT_HOURS + submit.getClientId() + CacheConstant.SEPARATE + submit.getMobile();

        int retry = 0;
        while (!cacheClient.zadd(key, submit.getOneHourLimitMilli(), submit.getOneHourLimitMilli())) {
            if (retry == RETRY_COUNT) break;
            retry++;
            submit.setOneHourLimitMilli(System.currentTimeMillis());
        }

        if (retry == RETRY_COUNT) {
            log.info("【Strategy Module - One-Hour Rate Limiting】  Insertion failed! retry count reached the limit but still failed, cannot send!");
            sendErrorMsg(submit);
        }

        long start = submit.getOneHourLimitMilli() - ONE_HOUR;
        int count = cacheClient.zRangeByScoreCount(key, Double.parseDouble(start + ""), Double.parseDouble(submit.getOneHourLimitMilli() + ""));

        if (count > LIMIT_HOUR) {
            log.info("【Strategy Module - One-Hour Rate Limiting】  the number of SMS in one hour exceeds the limit, cannot send!");
            cacheClient.zRemove(key, submit.getOneHourLimitMilli() + "");
            sendErrorMsg(submit);
        }

        log.info("【Strategy Module - One-Hour Rate Limiting】  Rule passed, SMS can be sent!");
    }

    private void sendErrorMsg(StandardSubmit submit) {
        String errorMessage = ExceptionEnums.ONE_HOUR_LIMIT + ",mobile = " + submit.getMobile();
        errorSendMsgUtil.sendErrorMessage(submit, ExceptionEnums.ONE_HOUR_LIMIT, errorMessage);
    }
}




















