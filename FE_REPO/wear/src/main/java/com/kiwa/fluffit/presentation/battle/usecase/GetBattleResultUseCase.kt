package com.kiwa.fluffit.presentation.battle.usecase

import com.kiwa.fluffit.presentation.battle.BattleRepository
import javax.inject.Inject

class GetBattleResultUseCase @Inject constructor(private val battleRepository: BattleRepository) {
    suspend operator fun invoke(battleId: String, score: Int) = battleRepository.getBattleResult(
        battleId,
        score
    )
}
