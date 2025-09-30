package com.peng.sms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "beacon-search")
public interface SearchClient {

    @PostMapping("/search/sms/list")
    Map<String, Object> findSmsByParameters(@RequestBody Map<String, Object> parameters);

}
