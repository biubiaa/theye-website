package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class BeanConfig {
    @Bean(name = "jedis")
    public Jedis getJedis(){
        Jedis jedis = new Jedis("127.0.0.1");
        jedis.auth("123456");
        return jedis;
    }
}
