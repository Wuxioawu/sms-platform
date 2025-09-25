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

@Service(value = "blackGlobal")
@Slf4j
public class BlackGlobalStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    private final String TRUE = "1";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - BlackGlobalStrategyFilter] running ");
        String mobile = submit.getMobile();
        String value = cacheClient.getString(CacheConstant.BLACK + mobile);

        if (TRUE.equals(value)) {
            log.info("[Strategy Module - BlackGlobalStrategyFilter]  the number is black mobile = {}", mobile);
            String errorMessage = ExceptionEnums.BLACK_GLOBAL.getMsg() + ",mobile = " + mobile;
            sendMsgUtil.sendErrorMessage(submit, ExceptionEnums.BLACK_GLOBAL, errorMessage);
        }
        log.info("[Strategy Module - BlackGlobalStrategyFilter]¬   The current phone number is not in the blacklist!！");
    }
}
