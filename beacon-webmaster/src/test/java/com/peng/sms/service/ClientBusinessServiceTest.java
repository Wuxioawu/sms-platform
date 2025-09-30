package com.peng.sms.service;

import com.peng.sms.entity.ClientBusiness;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientBusinessServiceTest {

    @Autowired
    private ClientBusinessService clientBusinessService;

    @Test
    void findAll() {
        List<ClientBusiness> all = clientBusinessService.findAll();
        System.out.println(all);
    }

    @Test
    void findByUserId() {
        List<ClientBusiness> all = clientBusinessService.findByUserId(1);
        System.out.println(all);

    }
}