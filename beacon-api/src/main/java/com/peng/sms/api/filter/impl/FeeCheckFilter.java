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
import org.springframework.validation.ObjectError;

import static sun.security.krb5.Confounder.longValue;

/**
 * check the requestion paramenter fee
 * the rest of fee
 */
@Service(value = "fee")
@Slf4j
public class FeeCheckFilter implements CheckFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
     * 只要短信内容的文字长度小于等于70个字，按照一条计算
     * the max length of a message
     */
    private final int MAX_LENGTH = 70;

    /**
     * 如果短信内容的文字长度超过70，67字/条计算
     */
    private final int LOOP_LENGTH = 67;

    private final String BALANCE = "balance";


    @Override
    public void check(StandardSubmit submit) {
        log.info("{interface module} ->  {check fee filter} ->  is running");

        String text = submit.getText();
        int length = text.length();

        /*
         * Determine the length of the SMS content:
         * If it is less than or equal to 70 characters, it is counted as one message.
         * If it exceeds 70 characters, it is divided into segments of 67 characters each, and the total cost is calculated based on the number of segments.
         */

        if (length <= MAX_LENGTH) {
            submit.setFee(ApiConstant.SINGLE_FEE);
        } else {
            int strip = length % LOOP_LENGTH == 0 ? length / LOOP_LENGTH : length / LOOP_LENGTH + 1;
            submit.setFee(ApiConstant.SINGLE_FEE * strip);
        }

        long balance = ((Integer) cacheClient.hget(CacheConstant.CLIENT_BALANCE + submit.getClientId(), BALANCE)).longValue();

        if (balance >= submit.getFee()) {
            log.info("{interface module} ->  {check fee filter} -> the fee is enough");
            return;
        }

        log.info("{interface module} ->  {check fee filter} -> the fee is dead");
        throw new ApiException(ExceptionEnums.BALANCE_NOT_ENOUGH);
    }
}
