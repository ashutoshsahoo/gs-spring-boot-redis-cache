package com.ashu.practice;

import com.ashu.practice.entity.User;
import com.ashu.practice.service.impl.UserServiceImpl;
import com.ashu.practice.util.EncryptedRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@SpringBootApplication
@EnableCaching
public class GsSpringBootRedisCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsSpringBootRedisCacheApplication.class, args);
    }


    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                          ObjectMapper objectMapper) {
        RedisSerializationContext.SerializationPair<User> jsonSerializer =
//                RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, User.class));
        // Enable encrypted caching for sensitive data
                RedisSerializationContext.SerializationPair.fromSerializer(new EncryptedRedisSerializer<>(objectMapper, User.class));
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(
                        RedisCacheConfiguration.defaultCacheConfig()
                                .disableCachingNullValues()
                                  .entryTtl(Duration.ofMinutes(7))
                                .serializeValuesWith(jsonSerializer)
                )
                .build();
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(UserServiceImpl.REDIS_CACHE_NAME_USERS,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(2)))
                .withCacheConfiguration("dataCache",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
    }

}
