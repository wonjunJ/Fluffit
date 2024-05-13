package com.ssafy.fluffitflupet.config

import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class FeignConfig {
    @Bean
    fun customConverters(): HttpMessageConverters {
        val additional: HttpMessageConverter<*> = MappingJackson2HttpMessageConverter()
        return HttpMessageConverters(additional)
    }
}