package org.jeecg.modules.system.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.RedisData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 功能描述
 *
 * @author: 菜瓜皮
 * @date: 2022年10月23日 10:05
 */
@Component
public class CacheUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheUtil(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 设置TTL解决缓存穿透问题
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithTTL(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 逻辑过期解决缓存击穿问题
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 解决缓存穿透问题
     * @param <R>
     * @return
     */
//    public <R,QW> R getWithPassThrough(String keyPrefix, QW qw, Class<R> type, Function<QueryWrapper, R> dbFallback){
//        String key = keyPrefix + qw;
//        String json = stringRedisTemplate.opsForValue().get(key);
//        System.out.println(json);
//        if(StrUtil.isNotBlank(json)){
//            return JSONUtil.toBean(json,type);
//        }
//
//        if(json != null){
//            return null;
//        }
//
//        R r = dbFallback.apply();
//
//    }
}
