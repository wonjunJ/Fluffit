package com.kiwa.fluffit.home

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.main.Flupet

sealed class HomeViewState : ViewState {
    abstract val coin: Int
    abstract val flupet: Flupet
    abstract val nextFullnessUpdateTime: Long
    abstract val nextHealthUpdateTime: Long
    abstract val message: String

    data class Default(
        override val coin: Int = 0,
        override val flupet: Flupet = Flupet(
            0,
            0,
            "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
            "도끼보다토끼",
            "",
            "",
            false
        ),
        override val nextFullnessUpdateTime: Long = 0L,
        override val nextHealthUpdateTime: Long = 0L,
        override val message: String = ""
    ) : HomeViewState()

    data class FlupetNameEdit(
        override val coin: Int,
        override val flupet: Flupet,
        override val nextFullnessUpdateTime: Long,
        override val nextHealthUpdateTime: Long,
        override val message: String
    ) : HomeViewState()
}
