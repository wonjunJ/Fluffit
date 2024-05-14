package com.ssafy.fluffitflupet.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class RedissonConfig(private val env: Environment) {
    var host = env.getProperty("spring.data.redis.host", "redis")
    var port = env.getProperty("spring.data.redis.port", "6379")
    var pwd = env.getProperty("spring.data.redis.password", "")

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer().address = "redis://$host:$port"
        config.useSingleServer().password = pwd
        return Redisson.create(config)
    }
}