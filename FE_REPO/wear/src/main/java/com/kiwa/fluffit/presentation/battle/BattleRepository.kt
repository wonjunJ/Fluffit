package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.model.battle.BattleResultUIModel
import com.kiwa.fluffit.model.battle.BattleStatisticsUIModel
import com.kiwa.fluffit.model.battle.GameUIModel

interface BattleRepository {
    suspend fun getBattleStatistics(): Result<BattleStatisticsUIModel>
    suspend fun findMatching(): Result<GameUIModel>

    suspend fun getBattleResult(battleId: String, score: Int): Result<BattleResultUIModel>

    suspend fun cancelBattle(): Result<Unit>
}
