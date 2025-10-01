package com.peng.sms.controller;

import com.peng.sms.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
public class SmsSearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/search/sms/list")
    public Map<String, Object> findSmsByParameters(@RequestBody Map<String, Object> parameters) throws IOException {
        Map<String, Object> smsByParameters = searchService.findSmsByParameters(parameters);
        log.info("[Search Module] -> findSmsByParameters:  smsByParameters {}", smsByParameters);
        return smsByParameters;
    }
}
