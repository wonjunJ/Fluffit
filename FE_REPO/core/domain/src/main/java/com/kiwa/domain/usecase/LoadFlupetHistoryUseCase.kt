package com.kiwa.domain.usecase

import com.kiwa.domain.repository.FlupetRepository
import javax.inject.Inject

class LoadFlupetHistoryUseCase @Inject constructor(
    private val repository: FlupetRepository
) {
    suspend operator fun invoke() = repository.getHistory()
}
