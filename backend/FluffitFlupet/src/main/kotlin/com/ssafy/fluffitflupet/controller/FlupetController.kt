package com.ssafy.fluffitflupet.controller

import com.ssafy.fluffitflupet.dto.FullResponse
import com.ssafy.fluffitflupet.dto.HealthResponse
import com.ssafy.fluffitflupet.dto.MainInfoResponse
import com.ssafy.fluffitflupet.dto.NickRequest
import com.ssafy.fluffitflupet.service.FlupetService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.*
import kotlin.coroutines.CoroutineContext

@RestController
@RequestMapping("/flupet")
class FlupetController(
    private val flupetService: FlupetService
): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    @GetMapping("/info")
    suspend fun getMainInfo(@RequestHeader("userId") userId: String): MainInfoResponse? {
        return flupetService.getMainInfo(userId)
    }

    @PutMapping("/nickname")
    suspend fun updateNickname(@RequestHeader("userId") userId: String, @RequestBody nickname: NickRequest): Map<String, String> {
        val job = coroutineScope { launch { flupetService.updateNickname(userId, nickname.nickname) } }
        job.join()
        return mapOf("result" to "SUCCESS")
    }

    @GetMapping("/fullness")
    suspend fun getFullness(@RequestHeader("userId") userId: String): FullResponse {
        return flupetService.getFullness(userId)
    }

    @GetMapping("/health")
    suspend fun getHealth(@RequestHeader("userId") userId: String): HealthResponse {
        return flupetService.getHealth(userId)
    }
}