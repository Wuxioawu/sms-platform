package com.peng.sms.api.filter.impl;

import com.peng.sms.api.client.BeaconCacheClient;
import com.peng.sms.api.filter.CheckFilter;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.ApiException;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.Map;

/**
 * check the requestion paramenter ApiKey
 */
@Service(value = "apikey")
@Slf4j
public class ApikeyCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Override
    public void check(StandardSubmit submit) {
        log.info("{interface module} check apikey filter is running");
        // get the clientBusiness from redis
        Map clientBusiness = cacheClient.hGetAll(CacheConstant.CLIENT_BUSINESS + submit.getApikey());

        // check if invalidation
        if (clientBusiness == null || clientBusiness.isEmpty()) {
            log.info("{interface module} clientBusiness is null or empty ");
            throw new ApiException(ExceptionEnums.ERROR_APIKEY);
        }

        // put the data
        submit.setClientId(Long.parseLong(clientBusiness.get("id") + ""));
        submit.setRealIP(clientBusiness.get("ipAddress") + "");
        log.info("{interface module} get clientBusiness = {} ", clientBusiness);

    }
}
