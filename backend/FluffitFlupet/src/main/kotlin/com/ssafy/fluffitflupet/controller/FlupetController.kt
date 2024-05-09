package com.ssafy.fluffitflupet.controller

import com.ssafy.fluffitflupet.dto.*
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
    suspend fun getMainInfo(@RequestHeader("memberId") userId: String): MainInfoResponse? {
        return flupetService.getMainInfo(userId)
    }

    @PutMapping("/nickname")
    suspend fun updateNickname(@RequestHeader("memberId") userId: String, @RequestBody nickname: NickRequest): Map<String, String> {
        val job = coroutineScope { launch { flupetService.updateNickname(userId, nickname.nickname) } }
        job.join()
        return mapOf("result" to "SUCCESS")
    }

    @GetMapping("/fullness")
    suspend fun getFullness(@RequestHeader("memberId") userId: String): FullResponse {
        return flupetService.getFullness(userId)
    }

    @GetMapping("/health")
    suspend fun getHealth(@RequestHeader("memberId") userId: String): HealthResponse {
        return flupetService.getHealth(userId)
    }

    @GetMapping("/collection")
    suspend fun getPetCollection(@RequestHeader("memberId") userId: String): CollectionResponse {
        return flupetService.getPetCollection(userId)
    }

    @PostMapping("/new-egg")
    suspend fun generateFlupet(@RequestHeader("memberId") userId: String): GenFlupetResponse {
        return flupetService.generateFlupet(userId)
    }

    @PostMapping("/evolution")
    suspend fun evolveFlupet(@RequestHeader("memberId") userId: String): EvolveResponse {

    }
}