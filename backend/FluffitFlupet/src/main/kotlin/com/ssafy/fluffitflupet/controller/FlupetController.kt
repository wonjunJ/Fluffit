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
        return flupetService.evolveFlupet(userId)
    }

    @GetMapping("/history")
    suspend fun getMyPetHistory(@RequestHeader("memberId") userId: String): HistoryResponse {
        return flupetService.getMyPetHistory(userId)
    }

    //플러펫 수명 랭킹
    @GetMapping("/age")
    suspend fun getFlupetRank(@RequestHeader("memberId") userId: String): RankingResponse {
        return flupetService.getFlupetRank(userId)
    }

    //member service에서 배틀 랭킹 관련 필요한 정보 조회(member-service에서 요청)
    @GetMapping("/rank-info")
    suspend fun getFlupetBattleRank(@RequestParam("memberIds") memberIds: List<String>): List<RankFlupetInfoDto> {
        return flupetService.getFlupetBattleRank(memberIds)
    }

    @PutMapping("/pat")
    suspend fun patMyFlupet(@RequestHeader("memberId") userId: String): Map<String, String> {
        flupetService.patMyFlupet(userId)
        return mapOf("result" to "SUCCESS")
    }

    //battle-service에서 요청
    @GetMapping("/battle-info")
    suspend fun getBattleInfo(@RequestHeader("memberId") userId: String): BattleInfoDto {
        return flupetService.getBattleInfo(userId)
    }
}