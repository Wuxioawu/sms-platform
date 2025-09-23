package com.peng.sms.api.filter.impl;

import com.peng.sms.api.client.BeaconCacheClient;
import com.peng.sms.api.filter.CheckFilter;
import com.peng.sms.api.uitls.PhoneFormatCheckUtil;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.ApiException;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

/**
 * check the requestion paramenter mobile
 */
@Service(value = "mobile")
@Slf4j
public class MobileCheckFilter implements CheckFilter {


    @Override
    public void check(StandardSubmit submit) {
        log.info("{interface module} ->  {check mobileNumber filter} ->  is running");
        String mobile = submit.getMobile();
        if (!StringUtils.isEmpty(mobile) && PhoneFormatCheckUtil.isChinaPhone(mobile)) {
            log.info("{interface module} ->  {check mobileNumber filter} -> the mobile can be used mobile = {}", mobile);
            return;
        }
        log.info("{interface module} ->  {check mobileNumber filter} -> the number is error = {}", mobile);
        throw new ApiException(ExceptionEnums.ERROR_MOBILE);
    }
}
