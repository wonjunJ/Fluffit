package com.kiwa.domain

interface TokenManager {
    suspend fun getAccessToken(): String

    suspend fun getRefreshToken(): String

    suspend fun saveToken(accessToken: String, refreshToken: String)

    suspend fun deleteToken()
}