package com.ssafy.fluffitflupet.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class MemberServiceClientAsync(
    private val client: MemberServiceClient
) {
    suspend fun getUserCoin(userId: String): Int = withContext(Dispatchers.IO) {
        try {
            println("feign client 요청은 보냈지??")
            return@withContext client.getUserCoin(userId)
        }catch (e: Exception){
            println("feign client 오류 코드는 ${e.printStackTrace()}")
            return@withContext -1
        }
    }
}