package com.peng.sms.api.filter.impl;

import com.peng.sms.api.filter.CheckFilter;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 * check the requestion paramenter Sign
 */
@Service(value = "sign")
@Slf4j
public class SignCheckFilter implements CheckFilter {
    
    @Override
    public void check(StandardSubmit submit) {
       log.info("SignCheckFilter check");
    }
}
