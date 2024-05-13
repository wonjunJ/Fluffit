package com.ssafy.fluffitflupet.service

import com.ssafy.fluffitflupet.client.MemberServiceClient
import com.ssafy.fluffitflupet.client.MemberServiceClientAsync
import com.ssafy.fluffitflupet.dto.FeedingResponse
import com.ssafy.fluffitflupet.dto.FoodListResponse
import com.ssafy.fluffitflupet.error.ErrorType
import com.ssafy.fluffitflupet.exception.CustomBadRequestException
import com.ssafy.fluffitflupet.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service

@Service
class FoodService(
    private val foodRepository: FoodRepository,
    private val client: MemberServiceClientAsync,
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>
) {
    //lombok slf4j를 쓰기 위해
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    suspend fun getFeedList(userId: String): FoodListResponse = coroutineScope {
        val foods = ArrayList<FoodListResponse.Food>()
        val foodlist = withContext(Dispatchers.IO){ foodRepository.findAll() }
        val coinWait = async { client.getUserCoin(userId) }
        foodlist.collect{ value ->
            foods.add(FoodListResponse.Food(
                id = value.id,
                name = value.name,
                fullnessEffect = value.fullnessEffect,
                healthEffect = value.healthEffect,
                price = value.price,
                imageUrl = value.imgUrl,
                description = value.description
            ))
        }
        return@coroutineScope FoodListResponse(
            foods = foods,
            coin = coinWait.await().coin
        )
    }

    suspend fun feedToPet(userId: String, foodId: Long): FeedingResponse? {
        val food = withContext(Dispatchers.IO){ foodRepository.findById(foodId) }
        if (food == null){
            throw CustomBadRequestException(ErrorType.INVALID_FOODID)
        }

        return null
    }
}