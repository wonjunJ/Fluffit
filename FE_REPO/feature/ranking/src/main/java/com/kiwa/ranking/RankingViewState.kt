package com.kiwa.ranking

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.ranking.RankingInfo

sealed class RankingViewState : ViewState {

    data class AgeRanking(
        val ageRankingList: List<RankingInfo> = emptyList(),
        val myRanking: RankingInfo = RankingInfo(0, "", "", "", "")
    ) :
        RankingViewState()

    data class BattleRanking(
        val battleRankingList: List<RankingInfo> = emptyList(),
        val myRanking: RankingInfo = RankingInfo(0, "", "", "", "")
    ) :
        RankingViewState()
}
