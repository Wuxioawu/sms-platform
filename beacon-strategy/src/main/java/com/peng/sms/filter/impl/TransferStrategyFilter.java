package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service(value = "transfer")
@Slf4j
public class TransferStrategyFilter implements StrategyFilter {
    @Autowired
    private BeaconCacheClient cacheClient;

    // already transfer phone numbers
    private final Boolean TRANSFER = true;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - TransferStrategyFilter] running ");
        String mobile = submit.getMobile();
        String value = cacheClient.getString(CacheConstant.TRANSFER + mobile);

        if (!StringUtils.isEmpty(value)) {
            submit.setOperatorId(Integer.valueOf(value));
            submit.setIsTransfer(TRANSFER);
            log.info("[Strategy Module - TransferStrategyFilter] the current phone already transfer ");
            return;
        }
        log.info("[Strategy Module - TransferStrategyFilter] no anything transfer ");
    }
}
