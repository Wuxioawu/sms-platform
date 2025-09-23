package com.peng.sms.api.filter.impl;

import com.peng.sms.api.client.BeaconCacheClient;
import com.peng.sms.api.filter.CheckFilter;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 * check the requestion paramenter ApiKey
 */
@Service(value = "apikey")
@Slf4j
public class ApikeyCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient beaconCacheClient;

    @Override
    public void check(StandardSubmit submit) {
        log.info("ApikeyCheckFilter check");
    }


}
