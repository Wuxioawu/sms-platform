package com.peng.sms.controller;

import com.msb.framework.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Test redis configuration
 */
@RestController
public class TestController {

    @Autowired
    private RedisClient redisClient;

    @PostMapping("/test/set/{key}")
    public String set(@PathVariable String key, @RequestBody Map map) {
        redisClient.hSet(key, map);
        return "ok";
    }

    @GetMapping("/test/get/{key}")
    public Map get(@PathVariable String key) {
        return redisClient.hGetAll(key);
    }

    @PostMapping("test/pipeline")
    public String pipeline() {
        Map<String, Object> map = new HashMap();
        map.put("1888888", "beijing, beijing move");
        map.put("1888889", "beijing, beijing dianxing");
        redisClient.pipelined(operations -> {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                operations.opsForValue().set(entry.getKey(), entry.getValue().toString());
            }
        });
        return "ok";
    }

}
