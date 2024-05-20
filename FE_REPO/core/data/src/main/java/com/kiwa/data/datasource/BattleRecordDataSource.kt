package com.kiwa.data.datasource

import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics

interface BattleRecordDataSource {
    suspend fun loadUserBattleRecord(): Result<UserBattleHistory>

    suspend fun loadUserBattleStatistics(): Result<UserBattleStatistics>
}
