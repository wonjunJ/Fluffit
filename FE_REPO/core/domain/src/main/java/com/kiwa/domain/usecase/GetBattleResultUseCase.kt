package com.kiwa.domain.usecase

import com.kiwa.domain.repository.BattleRepository
import javax.inject.Inject

class GetBattleResultUseCase @Inject constructor(private val battleRepository: BattleRepository) {
    suspend operator fun invoke(battleId: String, score: Int) = battleRepository.getBattleResult(battleId, score)
}
