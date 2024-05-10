package com.kiwa.data.datasource

import com.kiwa.data.api.AuthService
import com.kiwa.data.api.NaverAuthService
import com.kiwa.data.api.NaverLoginService
import com.kiwa.fluffit.model.User
import com.kiwa.fluffit.model.user.request.NaverLoginRequest
import com.kiwa.fluffit.model.user.request.UserRequest
import com.kiwa.fluffit.model.user.response.TokenResponse
import com.kiwa.fluffit.model.user.response.UserResponse
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val authService: AuthService,
    private val naverAuthService: NaverAuthService,
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
    ): Result<TokenResponse> = runCatching {
        authService.signInNaver(NaverLoginRequest(userCode, signature))
    }

    override suspend fun signOutNaver(
        naverClientId: String,
        naverSecret: String,
        accessToken: String
    ): Result<Unit> = runCatching {
        naverAuthService.signOutWithNaver(
            naverClientId,
            naverSecret,
            accessToken
        )
    }

    override suspend fun loadUserName(): Result<UserResponse> =
        runCatching {
            authService.loadUserName()
        }

    override suspend fun saveNewUserName(name: String): Result<Unit> = runCatching {
        authService.saveNewUserName(UserRequest(name))
    }
}
