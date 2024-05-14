package com.ssafy.fluffitflupet.controller

import com.ssafy.fluffitflupet.dto.FeedingResponse
import com.ssafy.fluffitflupet.dto.FoodListResponse
import com.ssafy.fluffitflupet.service.FoodService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.apache.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.coroutines.CoroutineContext

@RestController
@RequestMapping("/food")
class FoodController(
    val foodService: FoodService
): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    @GetMapping("/list")
    suspend fun getFeedList(@RequestHeader("memberId") userId: String): FoodListResponse {
        return foodService.getFeedList(userId)
    }

    @PutMapping("/feeding/{foodId}")
    suspend fun feedToPet(@RequestHeader("memberId") userId: String, @PathVariable("foodId") foodId: Long): FeedingResponse {
        return foodService.feedToPet(userId, foodId)
    }
}