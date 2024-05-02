package com.kiwa.data.api

import com.kiwa.fluffit.model.user.request.NaverLoginRequest
import com.kiwa.fluffit.model.user.response.TokenResponse
import com.kiwa.fluffit.model.user.response.Tokens
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    //api 언제 나와요

    @POST("api/")
    suspend fun refreshUserToken(
        @Header("refreshToken") refreshToken: String,
        @Header("Authorization") accessToken: String
    ): TokenResponse

    @POST("api/auth/auto")
    suspend fun autoLogin(
        @Header("Authorization") accessToken: String
    ): TokenResponse

    @POST("api/login")
    suspend fun signInNaver(
        @Body naverLoginRequest: NaverLoginRequest
    ): Tokens
}
