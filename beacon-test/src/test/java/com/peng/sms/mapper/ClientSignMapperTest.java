package com.peng.sms.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.ClientSign;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientSignMapperTest {

    @Autowired
    private ClientSignMapper clientSignMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findByClientId() {
        List<ClientSign> list = clientSignMapper.findByClientId(1L);
        for (ClientSign clientSign : list) {
            System.out.println(clientSign);
        }
        ObjectMapper objectMapper = new ObjectMapper();

        List<Map> collect = list.stream().map(cs -> {
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(cs), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        cacheClient.sadd("client_sign:1", collect.toArray(new Map[]{}));
    }
}