package com.kiwa.data.datasource

import com.kiwa.data.api.BattleResultService
import com.kiwa.data.api.BattleService
import com.kiwa.data.api.MatchingService
import com.kiwa.fluffit.model.battle.MatchingResponse
import javax.inject.Inject

class BattleDataSourceImpl @Inject constructor(
    private val battleService: BattleService,
    private val matchingService: MatchingService,
    private val battleResultService: BattleResultService
) : BattleDataSource {
    override suspend fun getBattleLogs() {
        TODO("Not yet implemented")
    }

    override suspend fun getBattleStatistics() {
        TODO("Not yet implemented")
    }

    override suspend fun findMatch(): Result<MatchingResponse> =
        matchingService.getMatching().fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) }
        )
}
