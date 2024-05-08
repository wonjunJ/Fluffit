package com.ssafy.fluffitflupet.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class MemberServiceClientAsync(
    private val client: MemberServiceClient
) {
    suspend fun getUserCoin(userId: String): Int = withContext(Dispatchers.IO) {
        client.getUserCoin(userId)
    }
}