package com.kiwa.data.datasource

import com.kiwa.fluffit.model.ranking.AgeRankingResponse
import com.kiwa.fluffit.model.ranking.BattleRankingResponse

interface RankingDataSource {

    suspend fun fetchBattleRanking(): Result<BattleRankingResponse>

    suspend fun fetchAgeRanking(): Result<AgeRankingResponse>
}
