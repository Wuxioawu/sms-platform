package com.peng.sms.mapper;

import com.peng.sms.entity.ClientBusiness;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)

class ClientBusinessMapperTest {

    @Autowired
    private ClientBusinessMapper mapper;

    @Test
    void findById() {
        ClientBusiness byId = mapper.findById(1L);
        System.out.println(byId);
    }
}