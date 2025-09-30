package com.peng.sms.mapper;

import com.peng.sms.entity.SmsUser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SmsUserMapperTest {

    @Autowired
    private SmsUserMapper smsUserMapper;

    @Test
    void deleteByPrimaryKey() {
    }

    @Test
    void insert() {
        SmsUser smsUser = smsUserMapper.selectByPrimaryKey(1);
        System.out.println(smsUser);
    }

    @Test
    void insertSelective() {
    }

    @Test
    void selectByPrimaryKey() {
    }

    @Test
    void updateByPrimaryKeySelective() {
    }

    @Test
    void updateByPrimaryKey() {
    }
}