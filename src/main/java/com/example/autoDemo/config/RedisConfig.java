package com.example.autoDemo.config;

import com.example.autoDemo.data.StockResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;

@Configuration
public class RedisConfig {
    @Value("${REDIS_URL}")
    private String redisUrl;
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        System.out.println("redisUrl:"+ redisUrl);
        URI uri = URI.create(redisUrl);
        config.setHostName(uri.getHost());
        config.setPort(uri.getPort());
        String[] userInfo = uri.getUserInfo().split(":");
        config.setPassword(RedisPassword.of(userInfo[1]));


        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, StockResponse> redisTemplate() {
        RedisTemplate<String, StockResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // Key 序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // Value 使用 JSON 序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());


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
