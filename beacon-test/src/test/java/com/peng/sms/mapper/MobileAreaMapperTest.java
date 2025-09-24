package com.peng.sms.mapper;

import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.MobileArea;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class MobileAreaMapperTest {

    @Autowired
    private MobileAreaMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<MobileArea> list = mapper.findAll();
        Map<String, String> map = new HashMap(list.size());
        for (MobileArea mobileArea : list) {
            map.put("phase:" + mobileArea.getMobileNumber(), mobileArea.getMobileArea() + "," + mobileArea.getMobileType());
        }
        cacheClient.pipelineString(map);
    }
}