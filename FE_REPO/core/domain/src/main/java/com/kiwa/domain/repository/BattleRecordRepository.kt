package com.kiwa.domain.repository

import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics

interface BattleRecordRepository {
    suspend fun loadUserBattleRecord(): Result<UserBattleHistory>

    suspend fun loadUserBattleStatistics(): Result<UserBattleStatistics>
}
