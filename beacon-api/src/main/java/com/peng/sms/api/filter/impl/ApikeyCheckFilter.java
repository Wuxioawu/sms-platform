package com.peng.sms.api.filter.impl;

import com.peng.sms.api.filter.CheckFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 * check the requestion paramenter ApiKey
 */
@Service(value = "apikey")
@Slf4j
public class ApikeyCheckFilter implements CheckFilter {

    @Override
    public void check(Object obj) {
        log.info("ApikeyCheckFilter check");
    }
}
