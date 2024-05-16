package com.kiwa.fluffit.presentation.api

import com.kiwa.fluffit.model.battle.BattleResultResponse

interface BattleResultService {
    suspend fun getBattleResult(battleId: String, score: Int): Result<BattleResultResponse>
}
