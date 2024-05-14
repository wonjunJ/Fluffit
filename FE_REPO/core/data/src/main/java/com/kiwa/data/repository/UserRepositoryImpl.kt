package com.kiwa.data.repository

import com.kiwa.data.datasource.UserDataSource
import com.kiwa.data.util.calculateHmac
import com.kiwa.domain.TokenManager
import com.kiwa.domain.repository.UserRepository
import com.kiwa.fluffit.model.user.response.UserModificationResponse
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val tokenManager: TokenManager
) : UserRepository {
    override suspend fun checkAccessToken(): Result<Boolean> {
        val accessToken = tokenManager.getAccessToken()

        val result = runBlocking {
            if (accessToken == "") {
                Result.failure(exception = NullPointerException())
            } else {
                Result.success(accessToken)
            }
        }

        return result.fold(
            onSuccess = {
                Result.success(true)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun autoLogin(): Result<Unit> {
        val accessToken = tokenManager.getAccessToken()
        val result = runBlocking { userDataSource.autoLogin("Bearer $accessToken") }

        return result.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun signInNaver(code: String): Result<Unit> {
        val signature = calculateHmac("$code")
        val result = runBlocking { userDataSource.signInNaver(code, signature) }

        return result.fold(
            onSuccess = {
                tokenManager.saveToken(it.accessToken, it.refreshToken)
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun getNaverId(accessToken: String): Result<String> =
        userDataSource.getNaverLoginId(accessToken).fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun logout(): Result<Unit> {
        tokenManager.deleteToken()
        return Result.success(Unit)
    }

    override suspend fun signOut(
        naverClientId: String,
        naverSecret: String,
        accessToken: String
    ): Result<Unit> =
        userDataSource.signOutNaver(naverClientId, naverSecret, accessToken).fold(
            onSuccess = {
                tokenManager.deleteToken()
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun loadUserName(accessToken: String): Result<String> =
        userDataSource.loadUserName(accessToken).fold(
            onSuccess = {
                Result.success(it.nickName)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun updateUserName(
        accessToken: String,
        name: String
    ): Result<UserModificationResponse> =
        userDataSource.updateUserName(accessToken, name).fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
}
