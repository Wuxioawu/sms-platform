package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.StrategyException;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.uitl.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "blackClient")
@Slf4j
public class BlackClientStrategyFilter implements StrategyFilter {

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    @Autowired
    private BeaconCacheClient cacheClient;

    private final String TRUE = "1";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - BlackClientStrategyFilter] running ");

        String mobile = submit.getMobile();
        Long clientId = submit.getClientId();

        String value = cacheClient.getString(CacheConstant.BLACK + clientId + CacheConstant.SEPARATE + mobile);
        if (TRUE.equals(value)) {
            log.info("[Strategy Module - BlackClientStrategyFilter]   the mobile number stay int black list！ mobile = {}", mobile);
            submit.setErrorMsg(ExceptionEnums.BLACK_CLIENT + ",mobile = " + mobile);
            sendMsgUtil.sendWriteLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.BLACK_CLIENT);
        }
        log.info("[Strategy Module - BlackClientStrategyFilter]   the current mobile is not client black list! ");
    }
}
