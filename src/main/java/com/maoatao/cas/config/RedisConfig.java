package com.maoatao.cas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfig redis 配置
 *
 * @author MaoAtao
 * @date 2021-05-25 13:42:21
 */
@Configuration
public class RedisConfig {

    /**
     * redisTemplate 序列化使用的JdkSerializationRedisSerializer, 存储二进制字节码（和OAuth2 保持一致）
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> objectRedisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                             StringRedisSerializer stringRedisSerializer,
                                                             JdkSerializationRedisSerializer jdkSerializationRedisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer();
    }

    @Bean
    public JdkSerializationRedisSerializer jdkSerializationRedisSerializer(){
        return new JdkSerializationRedisSerializer();
    }
}