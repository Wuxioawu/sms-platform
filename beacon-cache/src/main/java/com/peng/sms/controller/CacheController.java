package com.peng.sms.controller;

import com.msb.framework.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class CacheController {

    private final String CACHEMODEL = "【Cache Model】";

    @Autowired
    private RedisClient redisClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping(value = "/cache/hmset/{key}")
    public void hmset(@PathVariable(value = "key") String key, @RequestBody Map<String, Object> map) {
        log.info(CACHEMODEL + " hmset methods，key = {}，value = {}", key, map);
        redisClient.hSet(key, map);
    }

    @PostMapping(value = "/cache/set/{key}")
    public void set(@PathVariable(value = "key") String key, @RequestParam(value = "value") Object value) {
        log.info(CACHEMODEL + " set methods，key = {}，value = {}", key, value);
        redisClient.set(key, value);
    }

    @PostMapping(value = "/cache/sadd/{key}")
    public void sadd(@PathVariable(value = "key") String key, @RequestBody Map<String, Object>... value) {
        log.info(CACHEMODEL + " sadd methods，key = {}，value = {}", key, value);
        redisClient.sAdd(key, value);
    }

    @PostMapping(value = "/cache/saddstr/{key}")
    public void saddStr(@PathVariable(value = "key") String key, @RequestBody String... value) {
        log.info(CACHEMODEL + " saddStr methods，key = {}，value = {}", key, value);
        redisClient.sAdd(key, value);
    }

    @PostMapping(value = "/cache/sinterstr/{key}/{sinterKey}")
    public Set<Object> sinterStr(@PathVariable(value = "key") String key, @PathVariable String sinterKey, @RequestBody String... value) {
        log.info(CACHEMODEL + " sinterStr ，key = {}，sinterKey = {}，value = {}", key, sinterKey, value);
        log.info("");
        //1. store data to set 
        redisClient.sAdd(key, value);
        // 2. get the key and sinterKey to get to intersect
        Set<Object> result = redisTemplate.opsForSet().intersect(key, sinterKey);
        //3、 delete key
        redisClient.delete(key);
        //4、 return the result
        return result;
    }


    @GetMapping("/cache/hgetall/{key}")
    public Map hGetAll(@PathVariable(value = "key") String key) {
        log.info(CACHEMODEL + " hGetAll，key ={} ", key);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        redisClient.hGetAll(key);
        Map<Object, Object> value = redisTemplate.opsForHash().entries(key);
        log.info(CACHEMODEL + " hGetAll，key ={}  value = {}", key, value);
        return value;
    }

    @GetMapping("/cache/hget/{key}/{field}")
    public Object hget(@PathVariable(value = "key") String key, @PathVariable(value = "field") String field) {
        log.info(CACHEMODEL + CACHEMODEL + " hget ，  key ={}，field = {} ", key, field);
        Object value = redisClient.hGet(key, field);
        log.info(CACHEMODEL + " hget ，  key ={}，field = {}  value = {}", key, field, value);
        return value;
    }

    @GetMapping("/cache/smember/{key}")
    public Set smember(@PathVariable(value = "key") String key) {
        log.info(CACHEMODEL + " smember ，  key ={} ", key);
        Set<Object> values = redisClient.sMembers(key);
        log.info(CACHEMODEL + " smember ，  key ={}  value = {}", key, values);
        return values;
    }

    @PostMapping("/cache/pipeline/string")
    public void pipelineString(@RequestBody Map<String, String> map) {
        log.info(CACHEMODEL + " pipelineString， map.size ={} ", map.size());
        redisClient.pipelined(operations -> {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                operations.opsForValue().set(entry.getKey(), entry.getValue());
            }
        });
    }

    @GetMapping(value = "/cache/get/{key}")
    public Object get(@PathVariable(value = "key") String key) {
        log.info(CACHEMODEL + " get ， key = {}", key);
        Object value = redisClient.get(key);
        log.info(CACHEMODEL + " get ， key = {} value = {}", key, value);
        return value;
    }

    @PostMapping(value = "/cache/zadd/{key}/{score}/{member}")
    public Boolean zadd(@PathVariable(value = "key") String key,
                        @PathVariable(value = "score") Long score,
                        @PathVariable(value = "member") Object member) {
        log.info(CACHEMODEL + " zaddLong ， key  = {}，score = {}，value = {}", key, score, member);
        Boolean result = redisClient.zAdd(key, member, score);
        return result;
    }

    @GetMapping(value = "/cache/zrangebyscorecount/{key}/{start}/{end}")
    public int zRangeByScoreCount(@PathVariable(value = "key") String key,
                                  @PathVariable(value = "start") Double start,
                                  @PathVariable(value = "end") Double end) {
        log.info(CACHEMODEL + " zRangeByScoreCount ， key = {},start = {},end = {}", key, start, end);
        Set<ZSetOperations.TypedTuple<Object>> values = redisTemplate.opsForZSet().rangeByScoreWithScores(key, start, end);
        if (values != null) {
            return values.size();
        }
        return 0;
    }

    @DeleteMapping(value = "/cache/zremove/{key}/{member}")
    public void zRemove(@PathVariable(value = "key") String key, @PathVariable(value = "member") String member) {
        log.info(CACHEMODEL + " zRemove ，key = {},member = {}", key, member);
        redisClient.zRemove(key, member);
    }

    @PostMapping(value = "/cache/hincrby/{key}/{field}/{delta}")
    public Long hIncrBy(@PathVariable(value = "key") String key,
                        @PathVariable(value = "field") String field,
                        @PathVariable(value = "delta") Long delta) {
        log.info(CACHEMODEL + " hIncrBy ，   key = {},field = {}，number = {}", key, field, delta);
        Long result = redisClient.hIncrementBy(key, field, delta);
        log.info(CACHEMODEL + " hIncrBy ，   key = {},field = {}，number = {}, result = {}", key, field, delta, result);
        return result;
    }

    @PostMapping(value = "/cache/keys/{pattern}")
    public Set<String> keys(@PathVariable String pattern) {
        log.info(CACHEMODEL + " keys ，pattern = {}", pattern);
        Set<String> keys = redisTemplate.keys(pattern);
        log.info(CACHEMODEL + " keys , pattern = {}, keys = {}", pattern, keys);
        return keys;
    }
}
