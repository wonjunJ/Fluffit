package com.kiwa.domain.usecase

import com.kiwa.domain.repository.FlupetRepository
import javax.inject.Inject

class EditFlupetNicknameUseCase @Inject constructor(
    private val flupetRepository: FlupetRepository
) {
    suspend operator fun invoke(nickName: String) = flupetRepository.editFlupetNickname(nickName)
}
