package com.kiwa.fluffit.home

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.flupet.FlupetStatus
import com.kiwa.fluffit.model.main.Flupet
import com.kiwa.fluffit.model.main.ImageUrls

sealed class HomeViewState : ViewState {
    abstract val coin: Int
    abstract val flupet: Flupet
    abstract val nextFullnessUpdateTime: Long
    abstract val nextHealthUpdateTime: Long
    abstract val message: String
    abstract val flupetStatus: FlupetStatus

    data class Default(
        override val coin: Int = 0,
        override val flupet: Flupet = Flupet(
            0,
            0,
            ImageUrls(),
            "",
            "",
            "",
            false
        ),
        override val nextFullnessUpdateTime: Long = 0L,
        override val nextHealthUpdateTime: Long = 0L,
        override val message: String = "",
        override val flupetStatus: FlupetStatus = FlupetStatus.None,
        val evolution: Boolean = false,
        val beforeImage: String = ""
    ) : HomeViewState()

    data class FlupetNameEdit(
        override val coin: Int,
        override val flupet: Flupet,
        override val nextFullnessUpdateTime: Long,
        override val nextHealthUpdateTime: Long,
        override val message: String,
        override val flupetStatus: FlupetStatus
    ) : HomeViewState()
}
