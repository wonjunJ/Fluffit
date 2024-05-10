package com.kiwa.domain.usecase

import com.kiwa.domain.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        naverClientId: String,
        naverSecret: String,
        accessToken: String
    ) = userRepository.signOut(
        naverClientId, naverSecret, accessToken
    )
}
