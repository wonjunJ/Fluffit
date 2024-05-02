package com.kiwa.fluffit.home

import com.kiwa.fluffit.home.composebase.ViewState
import com.kiwa.fluffit.model.Flupet

sealed class HomeViewState : ViewState {
    abstract val coin: Int
    abstract val flupet: Flupet
    abstract val nextFullnessUpdateTime: Long
    abstract val nextHealthUpdateTime: Long

    data class Default(
        override val coin: Int = 0,
        override val flupet: Flupet = Flupet(0, 0, "", "도끼보다토끼", "", "", false),
        override val nextFullnessUpdateTime: Long = 0L,
        override val nextHealthUpdateTime: Long = 0L
    ) : HomeViewState()

    data class Loading(
        override val coin: Int,
        override val flupet: Flupet,
        override val nextFullnessUpdateTime: Long,
        override val nextHealthUpdateTime: Long
    ) : HomeViewState()

    data class FlupetNameEdit(
        override val coin: Int,
        override val flupet: Flupet,
        override val nextFullnessUpdateTime: Long,
        override val nextHealthUpdateTime: Long
    ) : HomeViewState()
}
