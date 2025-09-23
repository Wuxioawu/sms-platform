package com.peng.sms.api.filter;


import com.peng.sms.model.StandardSubmit;

/**
 *  the parent interface for the Strategy Pattern
 */
public interface CheckFilter {
    void check(StandardSubmit submit);
}
