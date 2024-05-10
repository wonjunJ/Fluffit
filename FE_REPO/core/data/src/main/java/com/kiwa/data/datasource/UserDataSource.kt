package com.kiwa.data.datasource

import com.kiwa.fluffit.model.user.response.TokenResponse
import com.kiwa.fluffit.model.user.response.Tokens
import com.kiwa.fluffit.model.user.response.UserResponse

interface UserDataSource {

    suspend fun autoLogin(accessToken: String): Result<TokenResponse>

    suspend fun getNaverLoginId(accessToken: String): Result<String>

    suspend fun signInNaver(
        userCode: String,
        signature: String
    ): Result<Tokens>

    suspend fun signOutNaver(
        naverClientId: String,
        naverSecret: String,
        accessToken: String
    ): Result<Unit>

    suspend fun loadUserName(): Result<UserResponse>

    suspend fun saveNewUserName(name: String): Result<Unit>
}
