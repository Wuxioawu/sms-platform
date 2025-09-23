package com.peng.sms.api.filter.impl;

import com.peng.sms.api.client.BeaconCacheClient;
import com.peng.sms.api.filter.CheckFilter;
import com.peng.sms.constant.ApiConstant;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.ApiException;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * check the requestion paramenter Sign
 */
@Service(value = "sign")
@Slf4j
public class SignCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
     * sign start index
     */
    private final int SIGN_START_INDEX = 1;

    /**
     * the sign info
     */
    private final String CLIENT_SIGN_INFO = "signInfo";

    /**
     * the sign id
     */
    private final String SIGN_ID = "id";

    @Override
    public void check(StandardSubmit submit) {
        log.info("{interface module} ->  {check sign filter} ->  is running");

        //1. if have the sign info
        String text = submit.getText();
        if (!text.contains(ApiConstant.SIGN_PREFIX) || !text.contains(ApiConstant.SIGN_SUFFIX)) {
            log.info("{interface module} ->  {check sign filter} ->  no have sign info");
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }

        //2. sub the sign content from the text
        String signText = text.substring(SIGN_START_INDEX, text.indexOf(ApiConstant.SIGN_SUFFIX));
        if (StringUtils.isEmpty(signText)) {
            log.info("{interface module} ->  {check sign filter} ->  the sign text is empty");
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }

        //3. get information from redis
        Set<Map> set = cacheClient.smember(CacheConstant.CLIENT_SIGN + submit.getClientId());
        if (set == null || set.isEmpty()) {
            log.info("{interface module} ->  {check sign filter} ->  search from the redis is null");
            throw new ApiException(ExceptionEnums.ERROR_SIGN);
        }

        // check the detail information of sign
        for (Map map : set) {
            if (signText.equals(map.get(CLIENT_SIGN_INFO))) {
                submit.setSign(signText);
                submit.setSignId(Long.parseLong(map.get(SIGN_ID) + ""));
                log.info("{interface module} ->  {check sign filter} ->  find sign info : {}", text);
                return;
            }
        }

        //5. no have match sign
        log.info("{interface module} ->  {check sign filter} ->  no sign can be used : {}", text);
        throw new ApiException(ExceptionEnums.ERROR_SIGN);
    }
}
