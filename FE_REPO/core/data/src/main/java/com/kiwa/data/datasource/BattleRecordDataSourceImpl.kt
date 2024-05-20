package com.kiwa.data.datasource

import com.kiwa.data.api.BattleRecordService
import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics
import javax.inject.Inject

class BattleRecordDataSourceImpl @Inject constructor(
    private val battleRecordService: BattleRecordService
) : BattleRecordDataSource {
    override suspend fun loadUserBattleRecord(): Result<UserBattleHistory> =
        runCatching {
            battleRecordService.loadUserBattleRecord()
        }

    override suspend fun loadUserBattleStatistics(): Result<UserBattleStatistics> =
        runCatching {
            battleRecordService.loadUserBattleStatistics()
        }
}
