package com.kiwa.domain.repository

import com.kiwa.fluffit.model.battle.BattleLog
import com.kiwa.fluffit.model.battle.BattleLogModel

interface BattleRepository {

    fun getBattleLogs(): Result<List<BattleLog>>
    fun getBattleStatistics(): Result<BattleLogModel>
}
