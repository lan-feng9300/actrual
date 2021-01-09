package com.example.demo.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.*;

@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
@Configuration
public class MyCacheConfig {

    /**
     *  自定义, config, 配置文件关于缓存的配置会失效
     *  原来: @ConfigurationProperties(prifix = "spring.cache") --> defaultConfig与配置文件绑定
     *  处理方法: 将配置文件的配置, 移到config中
     *  1) @EnableConfigurationProperties(CacheProperties.class)
     *  2) 使用方法一: @Autowire
     *               CacheProperties cacheProperties;
     *     使用方法二: 在容器获取: redisCacheConfiguration(CacheProperties cacheProperties)
     *  3) 如下TODO 步骤3:
     *  4) 以上处理, application.yml 的配置就已经生效了
     */
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 自定义key 的序列化机制
        config = config.serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        // 自定义value 的序列化机制
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // TODO 步骤3:
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
