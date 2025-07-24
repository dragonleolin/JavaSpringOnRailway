package com.example.autoDemo.config;

import com.example.autoDemo.data.StockResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // Spring Boot 會自動從 properties 載入 host/port/password
    }


    @Bean
    public RedisTemplate<String, StockResponse> redisTemplate() {
        RedisTemplate<String, StockResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // Key 序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // Value 使用 JSON 序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
    @Bean(name = "countRedisTemplate")
    public RedisTemplate<String, Object> countRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }


}
