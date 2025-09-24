package com.peng.sms.filter.impl;

import com.peng.sms.client.BeaconCacheClient;
import com.peng.sms.constant.CacheConstant;
import com.peng.sms.constant.RabbitMQConstants;
import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import com.peng.sms.uitl.MobileOperatorUtil;
import com.peng.sms.util.OperatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service(value = "phase")
@Slf4j
public class PhaseStrategyFilter implements StrategyFilter {

    /**
     * cut the mobile phone number
     */
    private final int MOBILE_START = 0;
    private final int MOBILE_END = 7;

    private final String SEPARATE = ",";

    private final int LENGTH = 2;

    private final String UNKNOWN = "未知 未知,未知";

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private MobileOperatorUtil mobileOperatorUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - PhaseStrategyFilter] running ");

        // 1. Based on the first 7 digits of the phone number, query the phone number information.
        String preMobile = submit.getMobile().substring(MOBILE_START, MOBILE_END);
        String areaInfo = cacheClient.getString(CacheConstant.PHASE + preMobile);

        // 2. If no information is found, call a third-party API to query the corresponding phone number information.
        getMobileInfo:
        if (areaInfo == null || areaInfo.isEmpty()) {
            log.info("[Strategy Module - PhaseStrategyFilter] areaInfo from the redis is null ");
            areaInfo = mobileOperatorUtil.getMobileInfoBy360(preMobile);
            // 3. After retrieving the information from the third-party API, send a message to MQ and synchronize it to MySQL and Redis.
            if (!StringUtils.isEmpty(areaInfo)) {
                rabbitTemplate.convertAndSend(RabbitMQConstants.MOBILE_AREA_OPERATOR, submit.getMobile());
                break getMobileInfo;
            }
            log.info("[Strategy Module - PhaseStrategyFilter] areaInfo from the third-party is null");
            areaInfo = UNKNOWN;
        }

        // 4. Whether the information is retrieved from Redis or the third-party API, encapsulate it into the StandardSubmit object.
        String[] areaAndOperator = areaInfo.split(SEPARATE);
        if (areaAndOperator.length == LENGTH) {
            submit.setArea(areaAndOperator[0]);
            submit.setOperatorId(OperatorUtil.getOperatorIdByOperatorName(areaAndOperator[1]));
        }
    }
}










