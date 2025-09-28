package com.peng.sms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface CacheClient {

    @PostMapping(value = "/cache/keys/{pattern}")
    Set<String> keys(@PathVariable String pattern);

    @GetMapping("/cache/hgetall/{key}")
    Map hGetAll(@PathVariable(value = "key") String key);

}
