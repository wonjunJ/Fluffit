package com.kiwa.domain.repository

import com.kiwa.fluffit.model.ranking.RankingUIModel

interface RankingRepository {

    suspend fun getBattleRanking(): Result<RankingUIModel>
    suspend fun getAgeRanking(): Result<RankingUIModel>
}
