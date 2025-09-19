package com.peng.sms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import com.fasterxml.jackson.databind.Module;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Set the serialization method for RedisTemplate
 */
@Configuration
public class RedisConfig {

    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer<T> redisSerializer) {
        // 1. Build the RedisTemplate object
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();

        // 2. Set the RedisConnectionFactory to RedisTemplate
        redisTemplate.setConnectionFactory(connectionFactory);

        // 3. Configure the serialization method for Redis keys
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // 4. Configure the serialization method for Redis values (e.g., Date)
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);

        // 5. Ensure the configuration takes effect
        redisTemplate.afterPropertiesSet();

        // 6. Return the RedisTemplate
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        // 1. Build the Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // 2. Enable support for JDK8 date/time formats
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addDeserializer(LocalDate.class,new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(dateTimeFormatter))
                .addSerializer(LocalDate.class,new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(dateTimeFormatter));
        objectMapper.registerModule(timeModule);

        // make sure the serialization success
        // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 3. Create and Return  a Jackson2JsonRedisSerializer instance
        return new Jackson2JsonRedisSerializer<>(objectMapper,Object.class);
    }
}
