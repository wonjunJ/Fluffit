package com.kiwa.data.datasource

import com.kiwa.data.api.RankingService
import com.kiwa.fluffit.model.ranking.AgeRankingResponse
import com.kiwa.fluffit.model.ranking.BattleRankingResponse
import javax.inject.Inject

class RankingDataSourceImpl @Inject constructor(private val rankingService: RankingService) :
    RankingDataSource {
    override suspend fun fetchBattleRanking(): Result<BattleRankingResponse> =
        try {
            val response = rankingService.fetchBattleRanking()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 에러"))
        }

    override suspend fun fetchAgeRanking(): Result<AgeRankingResponse> =
        try {
            val response = rankingService.fetchAgeRanking()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 에러"))
        }
}
