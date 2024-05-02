package com.ssafy.fluffitflupet

import com.ssafy.fluffitflupet.error.FeignErrorDecoder
import feign.Logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import reactor.util.Loggers

@SpringBootApplication
//@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
class FluffitFlupetApplication

fun main(args: Array<String>) {
    runApplication<FluffitFlupetApplication>(*args)
}

@Bean
fun feignLoggerLevel(): Logger.Level {
    return Logger.Level.FULL
}

//@Bean
//fun getFeignErrorDecoder(): FeignErrorDecoder {
//    return FeignErrorDecoder()
//}
