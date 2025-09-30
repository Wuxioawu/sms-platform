package com.peng.sms.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SmsRoleMapperTest {

    @Autowired
    private SmsRoleMapper smsRoleMapper;

    @Test
    void findRoleNameByUserId() {
        Set<String> roleNameByUserId = smsRoleMapper.findRoleNameByUserId(1);
        System.out.println(roleNameByUserId);
    }
}