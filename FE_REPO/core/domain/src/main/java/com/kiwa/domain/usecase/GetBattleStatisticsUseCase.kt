package com.kiwa.domain.usecase

import com.kiwa.domain.repository.BattleRepository
import javax.inject.Inject

class GetBattleStatisticsUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke() = battleRepository.getBattleStatistics()
}
