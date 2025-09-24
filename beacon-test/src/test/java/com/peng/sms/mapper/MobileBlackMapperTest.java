package com.peng.sms.mapper;

import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.MobileBlack;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class MobileBlackMapperTest {

    @Autowired
    private MobileBlackMapper mobileBlackMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<MobileBlack> mobileBlackList = mobileBlackMapper.findAll();
        for (MobileBlack mobileBlack : mobileBlackList) {
            if (mobileBlack.getClientId() == 0) {
                // 平台级别的黑名单   black:手机号   作为key
                cacheClient.set("black:" + mobileBlack.getBlackNumber(), "1");
            } else {
                // 客户级别的黑名单   black:clientId:手机号
                cacheClient.set("black:" + mobileBlack.getClientId() + ":" + mobileBlack.getBlackNumber(), "1");
            }
        }
    }
}