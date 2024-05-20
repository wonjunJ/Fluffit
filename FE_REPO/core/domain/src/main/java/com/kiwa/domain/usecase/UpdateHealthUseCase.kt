package com.kiwa.domain.usecase

import com.kiwa.domain.repository.FlupetRepository
import javax.inject.Inject

class UpdateHealthUseCase @Inject constructor(
    private val flupetRepository: FlupetRepository
) {
    suspend operator fun invoke() = flupetRepository.updateHealth()
}
