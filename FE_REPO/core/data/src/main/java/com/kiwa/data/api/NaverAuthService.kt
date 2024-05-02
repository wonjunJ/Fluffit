package com.kiwa.data.api

import com.kiwa.fluffit.model.user.response.NaverSignOutResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverAuthService {

    //여긴 네이버 api부분
    @GET("token")
    suspend fun signOutWithNaver(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("access_token") accessToken: String,
        @Query("grant_type") grantType: String = "delete",
        @Query("service_provider") serviceProvider: String = "Naver"
    ): NaverSignOutResponse
}
