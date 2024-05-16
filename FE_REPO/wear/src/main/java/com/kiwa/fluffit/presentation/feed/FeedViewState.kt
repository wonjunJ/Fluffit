package com.kiwa.fluffit.presentation.feed

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.food.Feed

sealed class FeedViewState : ViewState {
    abstract val isLoading: Boolean
    abstract val isFeeding: Boolean
    abstract val foodList: List<Feed>
    abstract val coin: Int
    abstract val feedNum: Int
    abstract val message: String

    data class Init(
        override val foodList: List<Feed> = emptyList(),
        override val isLoading: Boolean = true,
        override val isFeeding: Boolean = false,
        override val coin: Int = 0,
        override val feedNum: Int = 0,
        override val message: String=""
    ) : FeedViewState()

    data class Default(
        override val foodList: List<Feed>,
        override val isLoading: Boolean = false,
        override val isFeeding: Boolean = false,
        override val coin: Int,
        override val feedNum: Int = 0,
        override val message: String=""
    ) : FeedViewState()

    data class SelectFood(
        override val foodList: List<Feed>,
        override val isLoading: Boolean = false,
        override val isFeeding: Boolean = true,
        override val coin: Int,
        override val feedNum: Int = 0,
        override val message: String=""
    ) : FeedViewState()
}
