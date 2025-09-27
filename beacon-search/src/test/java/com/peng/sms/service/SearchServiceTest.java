package com.peng.sms.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    void index() {
        try {
            searchService.index("sms_submit_log_2025", "3", "{\"clientId\": 3}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void exists() throws IOException {
        System.out.println(searchService.exists("sms_submit_log_2025", "381124303657107456"));
    }

    @Test
    void update() {
    }

    @Test
    void findSmsByParameters() {
    }
}