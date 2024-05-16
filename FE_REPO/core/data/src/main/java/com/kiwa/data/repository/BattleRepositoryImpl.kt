package com.kiwa.data.repository

import com.kiwa.data.datasource.BattleDataSource
import com.kiwa.domain.repository.BattleRepository
import com.kiwa.fluffit.model.battle.BattleLog
import com.kiwa.fluffit.model.battle.BattleLogModel
import com.kiwa.fluffit.model.battle.BattleResultUIModel
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.model.battle.toGameUIModel
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val battleDataSource: BattleDataSource
) : BattleRepository {
    override suspend fun getBattleLogs(): Result<List<BattleLog>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBattleStatistics(): Result<BattleLogModel> {
        TODO("Not yet implemented")
    }

    override suspend fun findMatching(): Result<GameUIModel> =
        battleDataSource.findMatch().fold(
            onSuccess = { Result.success(it.toGameUIModel()) },
            onFailure = { Result.failure(it) }
        )

    override suspend fun getBattleResult(
        battleId: String,
        score: Int
    ): Result<BattleResultUIModel> =
        battleDataSource.getBattleResult(battleId, score).fold(
            onSuccess = {
                Result.success(
                    BattleResultUIModel(
                        it.isWin,
                        it.opponentBattleScore,
                        it.myBattleScore,
                        it.battlePoint,
                        it.battlePointChanges
                    )
                )
            },
            onFailure = { Result.failure(it) }
        )
}
