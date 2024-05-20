package com.kiwa.fluffit.presentation.feed

import com.kiwa.fluffit.base.ViewEvent
import com.kiwa.fluffit.presentation.battle.BattleViewEvent

sealed class FeedViewEvent : ViewEvent {
    data object InitLoadingFood : FeedViewEvent()

    data class SelectButton(val foodId: Int) : FeedViewEvent()

    data class FeedSelectedFood(val foodId: Int) : FeedViewEvent()

    data object OnDismissToast: FeedViewEvent()
}
