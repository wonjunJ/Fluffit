package com.kiwa.data.datasource

interface UserDataSource {

    suspend fun autoLogin() : Result<Unit>
//    suspend fun autoLogin(accessToken: String) : Result<TokenResponse>
}
