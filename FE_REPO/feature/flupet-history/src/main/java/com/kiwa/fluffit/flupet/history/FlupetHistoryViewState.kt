package com.kiwa.fluffit.flupet.history

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.flupet.response.MyFlupets

sealed class FlupetHistoryViewState : ViewState {
    abstract val isLoadingCollected: Boolean
    abstract val toastMessage: String

    data class Init(
        override val isLoadingCollected: Boolean = true,
        override val toastMessage: String = ""
    ) : FlupetHistoryViewState()

    data class Default(
        val flupetHistoryList: List<MyFlupets>,
        override val isLoadingCollected: Boolean = false,
        override val toastMessage: String = ""
    ) : FlupetHistoryViewState()
}
