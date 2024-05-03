package com.kiwa.ranking

import com.kiwa.fluffit.base.ViewState

sealed class RankingViewState : ViewState {

    data class AgeRanking(val ageRankingList: List<String>) : RankingViewState()

    data class BattleRanking(val battleRankingList: List<String>) : RankingViewState()
}
