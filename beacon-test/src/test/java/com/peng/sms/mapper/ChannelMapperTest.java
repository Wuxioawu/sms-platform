package com.peng.sms.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.Channel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
class ChannelMapperTest {

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<Channel> list = channelMapper.findAll();
        for (Channel channel : list) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = null;
            try {
                map = objectMapper.readValue(objectMapper.writeValueAsString(channel), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            cacheClient.hmset("channel:" + channel.getId(), map);
        }
    }
}