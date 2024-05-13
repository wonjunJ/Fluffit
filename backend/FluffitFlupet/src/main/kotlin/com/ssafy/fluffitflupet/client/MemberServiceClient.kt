package com.ssafy.fluffitflupet.client

import com.ssafy.fluffitflupet.dto.CoinDto
import com.ssafy.fluffitflupet.dto.Nick
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "member-service")
interface MemberServiceClient {
    @GetMapping("/member/get-coin")
    fun getUserCoin(@RequestHeader("memberId") memberId: String): CoinDto

    @GetMapping("/member/nickname")
    fun getNickname(@RequestHeader("memberId") memberId: String): Nick
}