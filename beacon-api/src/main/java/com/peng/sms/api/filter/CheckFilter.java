package com.peng.sms.api.filter;

import org.springframework.validation.ObjectError;

/**
 *  the parent interface for the Strategy Pattern
 */
public interface CheckFilter {

    /**
     * check
     * @param obj
     */
    void check(ObjectError obj);
}
