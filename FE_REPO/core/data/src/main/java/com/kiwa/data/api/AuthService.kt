package com.kiwa.data.api

import com.kiwa.fluffit.model.user.request.NaverLoginRequest
import com.kiwa.fluffit.model.user.request.UserRequest
import com.kiwa.fluffit.model.user.response.TokenResponse
import com.kiwa.fluffit.model.user.response.Tokens
import com.kiwa.fluffit.model.user.response.UserModificationResponse
import com.kiwa.fluffit.model.user.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {
    // api 언제 나와요

    @GET("member-service/auth/regenerate-token")
    suspend fun refreshUserToken(
        @Header("refreshToken") refreshToken: String
    ): Tokens

    @POST("member-service/autoLogin")
    suspend fun autoLogin(
        @Header("Authorization") accessToken: String
    ): TokenResponse

    @POST("member-service/auth/login")
    suspend fun signInNaver(
        @Body naverLoginRequest: NaverLoginRequest
    ): Tokens

    @GET("members/nickname")
    suspend fun loadUserName(): UserResponse

    @PUT("api/members/nickname")
    suspend fun saveNewUserName(
        @Body nickname : UserRequest
    ) : UserModificationResponse
}
