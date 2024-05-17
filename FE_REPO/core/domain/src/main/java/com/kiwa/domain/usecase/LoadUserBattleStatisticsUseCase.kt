package com.kiwa.domain.usecase

import com.kiwa.domain.repository.BattleRecordRepository
import javax.inject.Inject

class LoadUserBattleStatisticsUseCase @Inject constructor(
    private val repository: BattleRecordRepository
) {
    suspend operator fun invoke() = repository.loadUserBattleStatistics()
}
