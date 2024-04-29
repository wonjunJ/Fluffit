package com.kiwa.data.repository

import com.kiwa.data.datasource.UserDataSource
import com.kiwa.domain.TokenManager
import com.kiwa.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val tokenManager: TokenManager
) : UserRepository {
    override suspend fun checkAccessToken(): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun autoLogin(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(code: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getNaverId(accessToken: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(naverClientId: String, naverSecret: String, accessToken: String) {
        TODO("Not yet implemented")
    }
}