package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.StrategyException;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.uitl.ClientBalanceUtil;
import com.peng.sms.uitl.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "fee")
@Slf4j
public class FeeStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil errorSendMsgUtil;

    private final String BALANCE = "balance";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【Strategy Module - Balance Check】  Validating...");

        Long fee = submit.getFee();
        Long clientId = submit.getClientId();
        Long amount = cacheClient.hIncrBy(CacheConstant.CLIENT_BALANCE + clientId, BALANCE, -fee);

        // 3. Get the current client's debt limit (external method, temporarily hardcoded to 10000 cents)
        Long clientAmountLimit = ClientBalanceUtil.getClientAmountLimit(submit.getClientId());

        // 4. Check if the remaining balance after deduction exceeds the debt limit
        if (amount < clientAmountLimit) {
            log.info("【Strategy Module - Balance Check】  Balance after deduction exceeds allowed debt limit, cannot send SMS!");
            cacheClient.hIncrBy(CacheConstant.CLIENT_BALANCE + clientId, BALANCE, fee);
            errorSendMsgUtil.sendErrorMessage(submit, ExceptionEnums.BALANCE_NOT_ENOUGH, ExceptionEnums.BALANCE_NOT_ENOUGH.getMsg());
        }
        log.info("【Strategy Module - Balance Check】  Fee deducted successfully!");
    }
}
