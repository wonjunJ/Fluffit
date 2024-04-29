package com.kiwa.domain.usecase

import com.kiwa.domain.repository.UserRepository
import javax.inject.Inject

class TryAutoLoginUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend fun invoke() = repository.autoLogin()
}