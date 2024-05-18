package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.model.battle.BattleResultUIModel
import com.kiwa.fluffit.model.battle.BattleStatisticsUIModel
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.model.battle.toBattleStatisticsUIModel
import com.kiwa.fluffit.model.battle.toGameUIModel
import com.kiwa.fluffit.presentation.datasource.BattleDataSource
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val battleDataSource: BattleDataSource
) : BattleRepository {

    override suspend fun getBattleStatistics(): Result<BattleStatisticsUIModel> =
        battleDataSource.getBattleStatistics().fold(
            onSuccess = { Result.success(it.toBattleStatisticsUIModel()) },
            onFailure = { Result.failure(it) }
        )


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

    override suspend fun cancelBattle(): Result<Unit> = battleDataSource.cancelBattle().fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it) }
    )
}
