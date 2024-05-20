package com.ssafy.fluffitflupet.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.util.pattern.PathPatternParser

@Component
class CorsConfig {
    @Bean
    fun corsFilter(): CorsWebFilter {
        val config = CorsConfiguration()
        config.allowedOrigins= listOf("https://6507-14-46-142-190.ngrok-free.app/", "http://127.0.0.1:4040", "192.168.100.190", "168.126.63.1")
        //config.addAllowedOrigin("https://6507-14-46-142-190.ngrok-free.app/")  // 허용할 Origin
        config.addAllowedHeader("*")  // 모든 헤더 허용
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        //config.addAllowedMethod(arrayOf("GET","POST","PUT","PATCH","DELETE"))  // 모든 HTTP 메소드 허용
        config.allowCredentials = true  // 크로스 오리진 요청에서 사용자 인증 정보 허용

        val source = UrlBasedCorsConfigurationSource(PathPatternParser())
        source.registerCorsConfiguration("/**", config)  // 모든 경로에 대해 CORS 설정 적용

        return CorsWebFilter(source)
    }
}