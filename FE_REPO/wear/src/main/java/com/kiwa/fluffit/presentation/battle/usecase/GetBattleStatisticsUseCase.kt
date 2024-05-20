package com.kiwa.fluffit.presentation.battle.usecase

import com.kiwa.fluffit.presentation.battle.BattleRepository
import javax.inject.Inject

class GetBattleStatisticsUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke() = battleRepository.getBattleStatistics()
}
