package com.peng.sms.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SmsMenuMapperTest {

    @Autowired
    private SmsMenuMapper smsMenuMapper;

    @Test
    void findMenuByUserId() {
        List<Map<String, Object>> menuByUserId = smsMenuMapper.findMenuByUserId(1);
        for (Map<String, Object> map : menuByUserId) {
            System.out.println(map);
        }
    }
}