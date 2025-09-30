package com.peng.sms.controller;

import com.peng.sms.entity.SmsUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsUserController {

    @PostMapping("/login")
    public void login(@RequestBody SmsUser smsUser, String password,String ) {

    }

}
