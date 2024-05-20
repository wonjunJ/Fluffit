package com.kiwa.data.repository

import com.kiwa.data.datasource.BattleRecordDataSource
import com.kiwa.domain.repository.BattleRecordRepository
import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics
import javax.inject.Inject

class BattleRecordRepositoryImpl @Inject constructor(
    private val battleRecordDataSource: BattleRecordDataSource
) : BattleRecordRepository {
    override suspend fun loadUserBattleRecord(): Result<UserBattleHistory> =
        battleRecordDataSource.loadUserBattleRecord().fold(
            onSuccess = { response ->
                Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun loadUserBattleStatistics(): Result<UserBattleStatistics> =
        battleRecordDataSource.loadUserBattleStatistics().fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
}
