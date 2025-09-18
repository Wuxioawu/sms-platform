package com.peng.sms.api.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RefreshScope
public class CheckFilterContext {

    @Autowired
    private Map<String, CheckFilter> checkFilters;

    @Value("${filters:apikey,ip,sign,template}")
    private String filters;

    /**
     * to manage the order of the check
     */
    public void check(Object object) {
        // split the filters base on ,
        String[] checkItems = filters.split(",");

        for (String items : checkItems) {
            checkFilters.get(items).check(object);
        }
    }
}
