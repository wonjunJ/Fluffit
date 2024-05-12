package com.kiwa.domain.usecase

import com.kiwa.domain.repository.FlupetRepository
import com.kiwa.fluffit.model.main.MainUIModel
import javax.inject.Inject

class GetMainUIInfoUseCase @Inject constructor(
    private val flupetRepository: FlupetRepository
) {
    suspend operator fun invoke(): Result<MainUIModel> = flupetRepository.getMainUIInfo()
}
