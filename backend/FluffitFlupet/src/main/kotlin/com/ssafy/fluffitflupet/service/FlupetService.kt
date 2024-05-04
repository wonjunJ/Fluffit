package com.ssafy.fluffitflupet.service

import com.ssafy.fluffitflupet.dto.MainInfoResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FlupetService {
    //lombok slf4j를 쓰기 위해
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    suspend fun getMainInfo(userId: String): MainInfoResponse {

    }
}