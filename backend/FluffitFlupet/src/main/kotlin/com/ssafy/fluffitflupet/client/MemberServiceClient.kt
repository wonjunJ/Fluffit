package com.ssafy.fluffitflupet.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "member-service")
interface MemberServiceClient {
    @GetMapping("/{userId}")
    fun getUserCoin(@PathVariable("userId") userId: String): Int
}