package com.kiwa.fluffit.presentation.datasource

import com.kiwa.fluffit.model.battle.BattleResultResponse
import com.kiwa.fluffit.model.battle.MatchingResponse
import org.json.JSONObject
import javax.inject.Inject

class BattleDataSourceImpl @Inject constructor(
    private val battleService: com.kiwa.fluffit.presentation.api.BattleService,
    private val matchingService: com.kiwa.fluffit.presentation.api.MatchingService,
    private val battleResultService: com.kiwa.fluffit.presentation.api.BattleResultService
) : BattleDataSource {
    override suspend fun getBattleStatistics() = try {
        val response = battleService.getBattleStatistics()
        if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            val errorBodyStr = response.errorBody()?.string()
            val errorMsg = JSONObject(errorBodyStr.toString()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    } catch (e: Exception) {
        Result.failure(Exception("네트워크 에러"))
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

    override suspend fun cancelBattle(): Result<Unit> = try {
        val response = battleService.cancelBattle()
        if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            val errorBodyStr = response.errorBody()?.string()
            val errorMsg = JSONObject(errorBodyStr.toString()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    } catch (e: Exception) {
        Result.failure(Exception("네트워크 에러"))
    }
}
