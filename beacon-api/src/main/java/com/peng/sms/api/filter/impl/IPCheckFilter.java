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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * check the requestion paramenter IP
 * 这里的白名单相当于客户自己设置的一个表
 */
@Service(value = "ip")
@Slf4j
public class IPCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    private final String IP_ADDRESS = "ipAddress";

    @Override
    public void check(StandardSubmit submit) {
        log.info("{interface module} ->  {check ip filter} ->  is running");

        // check the ip adress, according the apikey and ipaddress
        String ip = cacheClient.hgetString(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), IP_ADDRESS);

        if (submit.getIp().isEmpty()) {
            submit.setIp(new ArrayList<>());
        }
        submit.getIp().add(ip);

        // check ip
        if (StringUtils.isEmpty(ip) || ip.contains(submit.getRealIP())) {
            log.info("{interface module} ->  {check ip filter} ->  the ip is know");
            return;
        }

        // the ip is invaildation
        log.info("{interface module} ->  {check ip filter} ->  the ip is error");
        throw new ApiException(ExceptionEnums.IP_NOT_WHITE);
    }
}
