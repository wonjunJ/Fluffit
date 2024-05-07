package com.kiwa.data.repository

import com.kiwa.data.datasource.RankingDataSource
import com.kiwa.domain.repository.RankingRepository
import com.kiwa.fluffit.model.ranking.RankingInfo
import com.kiwa.fluffit.model.ranking.RankingUIModel
import javax.inject.Inject

class RankingRepositoryImpl @Inject constructor(
    private val rankingDataSource: RankingDataSource
) : RankingRepository {
    override suspend fun getBattleRanking(): Result<RankingUIModel> =
        rankingDataSource.fetchBattleRanking().fold(
            onSuccess = {
                val rankingList = it.data.ranking.map { info ->
                    RankingInfo(
                        info.rank,
                        info.userName,
                        "${info.battlePoint}점",
                        info.petName,
                        info.petImageUrl
                    )
                }
                val myRank = RankingInfo(
                    it.data.myRank.rank,
                    it.data.myRank.userName,
                    "${it.data.myRank.battlePoint}점",
                    it.data.myRank.petName,
                    it.data.myRank.petImageUrl
                )
                Result.success(RankingUIModel(rankingList, myRank))
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun getAgeRanking(): Result<RankingUIModel> =
        rankingDataSource.fetchAgeRanking().fold(
            onSuccess = {
                val rankingList = it.data.ranking.map { info ->
                    RankingInfo(
                        info.rank,
                        info.userName,
                        "${info.lifeTime}시간",
                        info.petName,
                        info.petImageUrl
                    )
                }
                val myRank = RankingInfo(
                    it.data.myRank.rank,
                    it.data.myRank.userName,
                    "${it.data.myRank.lifeTime}시간",
                    it.data.myRank.petName,
                    it.data.myRank.petImageUrl
                )
                Result.success(RankingUIModel(rankingList, myRank))
            },
            onFailure = {
                return Result.failure(it)
            }
        )
}
