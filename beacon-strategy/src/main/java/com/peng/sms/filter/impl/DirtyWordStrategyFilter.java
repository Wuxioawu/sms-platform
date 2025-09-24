package com.peng.sms.filter.impl;

import com.peng.sms.filter.StrategyFilter;
import com.peng.sms.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "dirtyword")
@Slf4j
public class DirtyWordStrategyFilter implements StrategyFilter {
    @Override
    public void strategy(StandardSubmit submit) {
        log.info("[Strategy Module - DirtyWordStrategyFilter] running ");
    }
}
