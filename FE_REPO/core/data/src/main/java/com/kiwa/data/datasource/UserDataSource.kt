package com.kiwa.data.datasource

import com.kiwa.fluffit.model.user.response.TokenResponse

interface UserDataSource {

    //    suspend fun autoLogin() : Result<Unit>
    suspend fun autoLogin(accessToken: String): Result<TokenResponse>

    suspend fun getNaverLoginId(accessToken: String): Result<String>

    suspend fun signInNaver(
        userCode: String,
        signature: String,
        provider: String
    ): Result<TokenResponse>
}
