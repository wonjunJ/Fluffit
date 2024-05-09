package com.kiwa.domain.usecase

import com.kiwa.domain.repository.MainRepository
import com.kiwa.fluffit.model.main.MainUIModel
import javax.inject.Inject

class GetMainUIInfoUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(): Result<MainUIModel> = mainRepository.getMainUIInfo()
}
