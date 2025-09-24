package com.peng.sms.filter;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.exception.ApiException;
import com.peng.sms.exception.StrategyException;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.peng.sms.enums.ExceptionEnums.UNKNOWN_ERROR;

@Component
@Slf4j
public class StrategyFilterContext {

    @Autowired
    private Map<String, StrategyFilter> strategyFilterMap;

    @Autowired
    private BeaconCacheClient beaconCacheClient;

    private final String CLIENT_FILTERS = "clientFilters";

    public void strategy(StandardSubmit submit) {
        // 1. Retrieve the customer's verification information based on Redis
        String clientFilters = beaconCacheClient.hget(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), CLIENT_FILTERS);

        if (clientFilters == null) {
            log.info("[Strategy Module - StrategyFilterContext] check client information from the Redis is null ");
            throw new StrategyException(UNKNOWN_ERROR);
        }

        String[] clientFilterArray = clientFilters.split(",");
        if (clientFilterArray.length == 0) {
            log.info("[Strategy Module - StrategyFilterContext] clientFilterArray is null ");
            throw new StrategyException(UNKNOWN_ERROR);
        }

        // 2. After performing robustness checks, iterate by splitting with commas
        for (String clientFilter : clientFilterArray) {
            StrategyFilter strategyFilter = strategyFilterMap.get(clientFilter);
            if (strategyFilter != null) {
                strategyFilter.strategy(submit);
            }
        }
    }
}



