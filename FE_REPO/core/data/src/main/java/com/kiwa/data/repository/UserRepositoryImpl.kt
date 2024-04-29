package com.kiwa.data.repository

import com.kiwa.data.datasource.UserDataSource
import com.kiwa.domain.TokenManager
import com.kiwa.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val tokenManager: TokenManager
) : UserRepository {
    override suspend fun checkAccessToken(): Result<Boolean> {
        val accessToken = tokenManager.getAccessToken()

        val result = runBlocking {
            if (accessToken == "") Result.failure(exception = NullPointerException())
            else Result.success(accessToken)
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
        val result = runBlocking { userDataSource.autoLogin() }

//        return result.fold(
//            onSuccess = {
//                tokenManager.saveToken(it.data.)
//            }
//        )
        return result
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
