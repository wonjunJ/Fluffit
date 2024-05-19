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
                        info.userNickname,
                        "${info.battlePoint}점",
                        info.flupetNickname,
                        if (info.flupetImageUrl.size > 1) info.flupetImageUrl[1] else info.flupetImageUrl[0]
                    )
                }
                val myRank = RankingInfo(
                    it.myRank.rank,
                    it.myRank.userNickname,
                    "${it.myRank.battlePoint}점",
                    it.myRank.flupetNickname,
                    if (it.myRank.flupetImageUrl.size > 1) it.myRank.flupetImageUrl[1] else it.myRank.flupetImageUrl[0]
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
                        if (info.imageUrl.size > 1) info.imageUrl[1] else info.imageUrl[0]

                    )
                }
                val myRank = RankingInfo(
                    it.myRank.rank,
                    it.myRank.userNickname,
                    "${it.myRank.lifetime}시간",
                    it.myRank.flupetNickname,
                    if (it.myRank.imageUrl.size > 1) it.myRank.imageUrl[1] else it.myRank.imageUrl[0]

                )
                Result.success(RankingUIModel(rankingList, myRank))
            },
            onFailure = {
                return Result.failure(it)
            }
        )
}
