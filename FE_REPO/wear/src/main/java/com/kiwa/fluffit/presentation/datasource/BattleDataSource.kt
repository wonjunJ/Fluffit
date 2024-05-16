package com.kiwa.fluffit.presentation.datasource

import com.kiwa.fluffit.model.battle.BattleResultResponse
import com.kiwa.fluffit.model.battle.MatchingResponse

interface BattleDataSource {

    suspend fun getBattleLogs()

    suspend fun getBattleStatistics()

    suspend fun findMatch(): Result<MatchingResponse>

    suspend fun getBattleResult(battleId: String, score: Int): Result<BattleResultResponse>
}
