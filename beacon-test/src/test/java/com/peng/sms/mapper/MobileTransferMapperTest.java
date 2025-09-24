package com.peng.sms.mapper;

import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.MobileTransfer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class MobileTransferMapperTest {

    @Autowired
    private MobileTransferMapper mobileTransferMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<MobileTransfer> list = mobileTransferMapper.findAll();
        for (MobileTransfer mobileTransfer : list) {
            System.out.println("transfer:" + mobileTransfer.getTransferNumber());
            System.out.println(mobileTransfer.getNowIsp());
            cacheClient.set("transfer:" + mobileTransfer.getTransferNumber(), mobileTransfer.getNowIsp());
        }
    }
}