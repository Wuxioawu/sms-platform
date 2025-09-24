package com.peng.sms.filter;

import com.peng.sms.model.StandardSubmit;

public interface StrategyFilter {
    void strategy(StandardSubmit submit);
}
