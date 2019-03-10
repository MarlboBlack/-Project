package com.niaoren.eurekaclientsign.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisObjectSerializer redisObjectSerializer = new RedisObjectSerializer();

        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(redisObjectSerializer);
        return template;
    }
//
//    @Bean
//    public RedisTemplate<String, String> redisTemplate2(RedisConnectionFactory cf) {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
//        redisTemplate.setConnectionFactory(cf);
//        return redisTemplate;
//    }


}
