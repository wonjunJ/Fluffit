package com.kiwa.data.util

import android.content.SharedPreferences
import com.kiwa.domain.TokenManager
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val tokenPreferences: SharedPreferences
) : TokenManager {
    override suspend fun getAccessToken(): String =
        tokenPreferences.getString("accessToken", "") ?: ""


    override suspend fun getRefreshToken(): String =
        tokenPreferences.getString("refreshToken", "") ?: ""


    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        with(tokenPreferences.edit()) {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            apply()
        }
    }

    override suspend fun deleteToken() {
        with(tokenPreferences.edit()) {
            remove("accessToken")
            remove("refreshToken")
            apply()
        }
    }

}