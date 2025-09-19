package com.peng.sms.controller;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Test redis configuration
 */
@RestController
public class TestController {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/test/set/{key}")
    public String set(@PathVariable String key, @RequestBody Map map){
        redisTemplate.opsForHash().putAll(key,map);
        return "ok";
    }

    @GetMapping("/test/get/{key}")
    public Map get(@PathVariable String key){
        return redisTemplate.opsForHash().entries(key);
    }
}
