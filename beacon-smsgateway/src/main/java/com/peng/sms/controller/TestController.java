package com.peng.sms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class TestController {

    @Resource
    private ThreadPoolExecutor cmppSubmitPool;

    @GetMapping("/test")
    public String test() {
        cmppSubmitPool.execute(() -> {
            System.out.println(Thread.currentThread().getName());
        });
        return "success";
    }

}
