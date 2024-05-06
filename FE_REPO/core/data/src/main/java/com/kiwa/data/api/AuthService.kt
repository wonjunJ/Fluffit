package com.kiwa.data.api

import com.kiwa.fluffit.model.user.request.NaverLoginRequest
import com.kiwa.fluffit.model.user.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    //api 언제 나와요

    @GET("api/members/refresh")
    suspend fun refreshUserToken(
        @Header("refreshToken") refreshToken: String,
//        @Header("Authorization") accessToken: String
    ): TokenResponse

    @POST("api/members/autoLogin")
    suspend fun autoLogin(
        @Header("Authorization") accessToken: String
    ): TokenResponse

    @POST("api/login")
    suspend fun signInNaver(
        @Body naverLoginRequest: NaverLoginRequest
    ): TokenResponse
}
