package com.kiwa.ranking

import com.kiwa.fluffit.base.ViewEvent

sealed class RankingViewEvent : ViewEvent {
    data object OnClickAgeRankingButton : RankingViewEvent()

    data object OnClickBattleRankingButton : RankingViewEvent()
}
