package com.ssafy.fluffitflupet.client

import com.ssafy.fluffitflupet.dto.CoinDto
import com.ssafy.fluffitflupet.dto.Nick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class MemberServiceClientAsync(
    private val client: MemberServiceClient
) {
    suspend fun getUserCoin(userId: String): CoinDto = withContext(Dispatchers.IO) {
        try {
            println("feign client 요청은 보냈지??")
            return@withContext client.getUserCoin(userId)
        }catch (e: Exception){
            println("feign client 오류 코드는 ${e.printStackTrace()}")
            return@withContext CoinDto(coin = -1)
        }
    }

    suspend fun getNickname(userId: String): Nick = withContext(Dispatchers.IO) {
        try {
            return@withContext client.getNickname(userId)
        }catch (e: Exception){
            println("닉네임 feign client 오류 코드는 ${e.printStackTrace()}")
            return@withContext Nick(nickname = "")
        }
    }
}