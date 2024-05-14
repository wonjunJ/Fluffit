package com.kiwa.domain.repository

import com.kiwa.fluffit.model.user.response.UserModificationResponse

interface UserRepository {
    suspend fun checkAccessToken(): Result<Boolean>

    suspend fun autoLogin(): Result<Unit>

    suspend fun signInNaver(code: String): Result<Unit>

    suspend fun getNaverId(accessToken: String): Result<String>

    suspend fun logout(): Result<Unit>

    suspend fun signOut(
        naverClientId: String,
        naverSecret: String,
        accessToken: String
    ): Result<Unit>

    suspend fun loadUserName(accessToken: String): Result<String>

    suspend fun updateUserName(accessToken: String, name: String): Result<UserModificationResponse>
}
