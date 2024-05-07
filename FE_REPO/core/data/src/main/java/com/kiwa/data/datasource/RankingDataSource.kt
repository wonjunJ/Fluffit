package com.kiwa.data.datasource

import com.kiwa.fluffit.model.NetworkResponse
import com.kiwa.fluffit.model.ranking.AgeRankingResponse
import com.kiwa.fluffit.model.ranking.BattleRankingResponse

interface RankingDataSource {

    suspend fun fetchBattleRanking(): Result<NetworkResponse<BattleRankingResponse>>

    suspend fun fetchAgeRanking(): Result<NetworkResponse<AgeRankingResponse>>
}
