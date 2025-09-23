package com.peng.sms.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.ClientBalance;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientBalanceMapperTest {

    @Autowired
    private ClientBalanceMapper clientBalanceMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findByClientId() throws JsonProcessingException {
        ClientBalance clientBalance = clientBalanceMapper.findByClientId(1L);

        System.out.println(clientBalance);

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientBalance), Map.class);

        cacheClient.hmset("client_balance:1", map);
    }
}