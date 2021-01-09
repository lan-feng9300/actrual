package com.example.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        // 单节点,实例, 参考redis 官网
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.101.19:6379").setPassword("123456");

        return Redisson.create(config);
    }
}
