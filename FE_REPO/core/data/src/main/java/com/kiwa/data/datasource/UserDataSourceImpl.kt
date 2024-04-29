package com.kiwa.data.datasource

import com.kiwa.data.api.AuthService
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val authService: AuthService
){

}
