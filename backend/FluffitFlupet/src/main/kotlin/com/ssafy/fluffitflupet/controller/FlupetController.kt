package com.ssafy.fluffitflupet.controller

import com.ssafy.fluffitflupet.dto.MainInfoResponse
import com.ssafy.fluffitflupet.service.FlupetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class FlupetController(
    private val flupetService: FlupetService
) {
    @GetMapping("/info")
    suspend fun getMainInfo(@RequestHeader("userId") userId: String): MainInfoResponse? {
        return flupetService.getMainInfo(userId)
    }
}