package com.kiwa.fluffit.home

import com.kiwa.fluffit.home.composebase.ViewState

sealed class HomeViewState : ViewState{
    abstract val isTryingAutoLogin: Boolean

    data class Default(
        override val isTryingAutoLogin: Boolean = false
    ) : HomeViewState()

    data class Ranking(
        override val isTryingAutoLogin: Boolean = false
    ) : HomeViewState()
}