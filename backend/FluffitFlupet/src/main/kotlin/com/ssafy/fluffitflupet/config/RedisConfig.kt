package com.ssafy.fluffitflupet.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun reactiveRedisTemplate(reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> {
        val context = StringRedisSerializer()
        return ReactiveRedisTemplate(reactiveRedisConnectionFactory, RedisSerializationContext.newSerializationContext<String, String>(context)
            .key(context)
            .value(context)
            .hashKey(context)
            .hashValue(context)
            .build())
    }
}