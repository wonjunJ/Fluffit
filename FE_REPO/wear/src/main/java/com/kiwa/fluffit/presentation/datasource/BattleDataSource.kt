package com.kiwa.fluffit.presentation.datasource

import com.kiwa.fluffit.model.battle.BattleResultResponse
import com.kiwa.fluffit.model.battle.BattleStatisticsResponse
import com.kiwa.fluffit.model.battle.MatchingResponse

interface BattleDataSource {

    suspend fun getBattleStatistics(): Result<BattleStatisticsResponse>

    suspend fun findMatch(): Result<MatchingResponse>

    suspend fun getBattleResult(battleId: String, score: Int): Result<BattleResultResponse>

    suspend fun cancelBattle(): Result<Unit>
}
