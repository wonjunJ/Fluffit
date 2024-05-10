package com.kiwa.domain.usecase

import com.kiwa.domain.repository.UserRepository
import javax.inject.Inject

class SaveNewUserNameUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(name: String) = repository.setUserName(name)
}
