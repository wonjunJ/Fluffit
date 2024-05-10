package com.kiwa.data.repository

import android.util.Log
import com.kiwa.data.datasource.UserDataSource
import com.kiwa.data.util.calculateHmac
import com.kiwa.domain.TokenManager
import com.kiwa.domain.repository.UserRepository
import com.kiwa.fluffit.model.User
import com.kiwa.fluffit.model.user.response.UserResponse
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "UserRepositoryImpl_싸피"

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
                tokenManager.saveToken(it.data.accessToken, it.data.refreshToken)
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun signInNaver(code: String): Result<Unit> {
        val signature = calculateHmac("$code&NAVER")

        Log.d(TAG, "signInNaver: code 값 : $code")

        // 강제 token 발급 성공 코드
        val result = runBlocking {
            Result.success("")
        }

        return result.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )

        // test를 위해 강제 token 발급 성공 process로 대신하여 테스트함. 추후에는 하단 주석코드를 사용하면 됩니다
//        val result = runBlocking { userDataSource.signInNaver(code, signature, "NAVER") }
//        return result.fold(
//            onSuccess = {
//                tokenManager.saveToken(it.accessToken, it.refreshToken)
//                Result.success(Unit)
//            },
//            onFailure = {
//                Result.failure(it)
//            }
//        )
    }

    override suspend fun getNaverId(accessToken: String): Result<String> =
        userDataSource.getNaverLoginId(accessToken).fold(
            onSuccess = {
                Log.d(TAG, "getNaverId: 네이버 ID 값 성공 : $it")
                Result.success(it)
            },
            onFailure = {
                Log.d(TAG, "getNaverId: 네이버 ID 값 실패 : $it")
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

    override suspend fun loadUserName(): Result<UserResponse> =
        userDataSource.loadUserName().fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )


    override suspend fun setUserName(name: String): Result<Unit> =
        userDataSource.saveNewUserName(name).fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )

}
