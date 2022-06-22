package com.example.demo.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * list lPop(json序列化)
     *
     * @param key 缓存key
     * @return object
     */
    public <T> T listLPopByJson(String key, Class<T> clazz) {
        try {
            String valueStr = stringRedisTemplate.opsForList().leftPop(key);
            if (StringUtils.isBlank(valueStr)) {
                return null;
            }
            return JSON.parseObject(valueStr, clazz);
        } catch (Exception e) {
            logger.error("listLPopByJson error key:{}", key, e);
            return null;
        }
    }


    /**
     * list rPush(json序列化)
     *
     * @param key 缓存key
     * @param valueList 需要缓存value集合
     * @param expiredSeconds 过期时间(单位为秒)
     */
    public void listRPushByJson(String key, List<?> valueList, long expiredSeconds) {
        if (StringUtils.isBlank(key) || CollectionUtils.isEmpty(valueList)) {
            return;
        }
        try {
            List<String> jsonValueList = valueList.stream().map(JSON::toJSONString).collect(Collectors.toList());
            stringRedisTemplate.opsForList().rightPushAll(key, jsonValueList);
            stringRedisTemplate.expire(key, expiredSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("listRPushByJson error key:{} valueList:{} expired:{}", key, valueList, expiredSeconds, e);
        }
    }
}
