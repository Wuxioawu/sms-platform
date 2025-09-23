package com.peng.sms.api.controller;

import com.peng.sms.api.filter.CheckFilterContext;
import com.peng.sms.model.StandardSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Deprecated
@Controller
public class TestController {

    @Autowired
    private CheckFilterContext checkFilterContext;

    @GetMapping("/api/test")
    public void test() {
        System.out.println("==================Test success================");
        checkFilterContext.check(new StandardSubmit());
    }

}
