package com.peng.sms.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.sms.client.CacheClient;
import com.peng.sms.entity.ClientTemplate;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientTemplateMapperTest {

    @Autowired
    private ClientTemplateMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findBySignId() {
        List<ClientTemplate> ct_15 = mapper.findBySignId(15L);
        List<ClientTemplate> ct_24 = mapper.findBySignId(24L);

        for (ClientTemplate ct : ct_15) {
            System.out.println(ct);
        }

        // no have data from in database
        for (ClientTemplate ct : ct_24) {
            System.out.println(ct);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Map> collect = ct_15.stream().map(ct -> {
            try {
                return mapper.readValue(mapper.writeValueAsString(ct), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        cacheClient.sadd("client_template:15", collect.toArray(new Map[]{}));
    }
}