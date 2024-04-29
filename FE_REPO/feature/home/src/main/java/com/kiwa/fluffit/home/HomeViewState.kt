package com.kiwa.fluffit.home

import com.kiwa.fluffit.home.composebase.ViewState
import com.kiwa.fluffit.model.Flupet

sealed class HomeViewState : ViewState {
    abstract val coin: Int
    abstract val flupet: Flupet?

    data class Default(
        override val coin: Int = 0, override val flupet: Flupet? = null,
    ) : HomeViewState()

    data class Loading(
        override val coin: Int = 0, override val flupet: Flupet,
    ) : HomeViewState()

}