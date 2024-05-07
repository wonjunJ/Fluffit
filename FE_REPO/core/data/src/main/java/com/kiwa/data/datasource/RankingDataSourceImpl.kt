package com.kiwa.data.datasource

import com.kiwa.data.api.RankingService
import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.ranking.AgeRankingResponse
import com.kiwa.fluffit.model.ranking.BattleRankingResponse
import javax.inject.Inject

class RankingDataSourceImpl @Inject constructor(private val rankingService: RankingService) :
    RankingDataSource {
    override suspend fun fetchBattleRanking(): Result<NetworkResponse<BattleRankingResponse>> =
        runCatching { rankingService.fetchBattleRanking() }

    override suspend fun fetchAgeRanking(): Result<NetworkResponse<AgeRankingResponse>> =
        runCatching { rankingService.fetchAgeRanking() }
}
