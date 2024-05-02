package com.kiwa.data.datasource

import com.kiwa.data.api.AuthService
import com.kiwa.data.api.NaverLoginService
import com.kiwa.fluffit.model.user.request.NaverLoginRequest
import com.kiwa.fluffit.model.user.response.TokenResponse
import com.kiwa.fluffit.model.user.response.Tokens
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val authService: AuthService,
    private val naverLoginService: NaverLoginService
) : UserDataSource {
    override suspend fun autoLogin(accessToken: String): Result<TokenResponse> = runCatching {
        authService.autoLogin(accessToken)
    }

    override suspend fun getNaverLoginId(accessToken: String): Result<String> = runCatching {
        naverLoginService.getNaverUserId("Bearer $accessToken").response.id
    }

    override suspend fun signInNaver(
        userCode: String,
        signature: String,
        provider: String
    ): Result<Tokens> = runCatching {
        authService.signInNaver(NaverLoginRequest(signature))
    }
}
