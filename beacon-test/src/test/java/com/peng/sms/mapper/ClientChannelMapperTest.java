package com.peng.sms.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.ClientChannel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientChannelMapperTest {

    @Autowired
    private ClientChannelMapper clientChannelMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<ClientChannel> list = clientChannelMapper.findAll();
        for (ClientChannel clientChannel : list) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = null;
            try {
                map = objectMapper.readValue(objectMapper.writeValueAsString(clientChannel), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            cacheClient.sadd("client_channel:" + clientChannel.getClientId(), map);
        }
    }
}