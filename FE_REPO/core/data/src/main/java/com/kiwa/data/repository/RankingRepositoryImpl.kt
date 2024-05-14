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
                val rankingList = it.ranking.map { info ->
                    RankingInfo(
                        info.rank,
                        info.userName,
                        "${info.battlePoint}점",
                        info.petName,
                        info.petImageUrl.first()
                    )
                }
                val myRank = RankingInfo(
                    it.myRank.rank,
                    it.myRank.userName,
                    "${it.myRank.battlePoint}점",
                    it.myRank.petName,
                    it.myRank.petImageUrl.first()
                )
                Result.success(RankingUIModel(rankingList, myRank))
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun getAgeRanking(): Result<RankingUIModel> =
        rankingDataSource.fetchAgeRanking().fold(
            onSuccess = {
                val rankingList = it.ranking.map { info ->
                    RankingInfo(
                        info.rank,
                        info.userNickname,
                        "${info.lifetime}시간",
                        info.flupetNickname,
                        info.imageUrl.first()
                    )
                }
                val myRank = RankingInfo(
                    it.myRank.rank,
                    it.myRank.userNickname,
                    "${it.myRank.lifetime}시간",
                    it.myRank.flupetNickname,
                    it.myRank.imageUrl.first()
                )
                Result.success(RankingUIModel(rankingList, myRank))
            },
            onFailure = {
                return Result.failure(it)
            }
        )
}
