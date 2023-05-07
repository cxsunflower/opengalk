package com.opengalk.server.配置类;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opengalk.server.实体类.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserInfo> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, UserInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                // 解决jackson2无法反序列化LocalDateTime的问题
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                        // 反序列化非final字段
                        ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        redisTemplate.setKeySerializer(stringRedisSerializer); // key的序列化类型
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value的序列化类型
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
