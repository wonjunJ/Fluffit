package com.kiwa.data.datasource

import com.kiwa.fluffit.model.battle.MatchingResponse

interface BattleDataSource {

    suspend fun getBattleLogs()

    suspend fun getBattleStatistics()

    suspend fun findMatch(): Result<MatchingResponse>
}
