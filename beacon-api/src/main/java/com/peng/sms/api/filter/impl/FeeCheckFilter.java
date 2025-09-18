package com.peng.sms.api.filter.impl;

import com.peng.sms.api.filter.CheckFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 * check the requestion paramenter fee
 * the rest of fee
 */
@Service(value = "fee")
@Slf4j
public class FeeCheckFilter implements CheckFilter {
    
    @Override
    public void check(Object obj) {
       log.info("FeeCheckFilter check");
    }
}
