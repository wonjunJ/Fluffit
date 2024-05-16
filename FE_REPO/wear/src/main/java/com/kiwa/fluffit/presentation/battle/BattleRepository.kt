package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.model.battle.BattleLog
import com.kiwa.fluffit.model.battle.BattleLogModel
import com.kiwa.fluffit.model.battle.BattleResultUIModel
import com.kiwa.fluffit.model.battle.GameUIModel

interface BattleRepository {

    suspend fun getBattleLogs(): Result<List<BattleLog>>
    suspend fun getBattleStatistics(): Result<BattleLogModel>
    suspend fun findMatching(): Result<GameUIModel>

    suspend fun getBattleResult(battleId: String, score: Int): Result<BattleResultUIModel>
}
