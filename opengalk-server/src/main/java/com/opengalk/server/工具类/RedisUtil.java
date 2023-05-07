package com.opengalk.server.工具类;

import com.opengalk.server.实体类.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    public final RedisTemplate<String, UserInfo> redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public void setObject(String key, UserInfo value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public UserInfo getObject(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     */
    public void deleteObject(String key) {
        redisTemplate.delete(key);
    }
}
