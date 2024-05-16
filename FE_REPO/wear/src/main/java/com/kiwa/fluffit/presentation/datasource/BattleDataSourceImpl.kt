package com.kiwa.fluffit.presentation.datasource

import com.kiwa.fluffit.model.battle.BattleResultResponse
import com.kiwa.fluffit.model.battle.MatchingResponse
import javax.inject.Inject

class BattleDataSourceImpl @Inject constructor(
    private val battleService: com.kiwa.fluffit.presentation.api.BattleService,
    private val matchingService: com.kiwa.fluffit.presentation.api.MatchingService,
    private val battleResultService: com.kiwa.fluffit.presentation.api.BattleResultService
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

    override suspend fun getBattleResult(
        battleId: String,
        score: Int
    ): Result<BattleResultResponse> =
        battleResultService.getBattleResult(battleId, score).fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) }
        )
}
