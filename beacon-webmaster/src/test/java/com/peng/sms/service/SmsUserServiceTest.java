package com.peng.sms.service;

import com.peng.sms.entity.SmsUser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SmsUserServiceTest {

    @Autowired
    private SmsUserService smsUserService;

    @Test
    void findByUsername() {
        SmsUser admin = smsUserService.findByUsername("admin");
        System.out.println(admin);
    }
}