package com.kiwa.fluffit.flupet.history

import com.kiwa.fluffit.base.ViewEvent

sealed class FlupetHistoryViewEvent : ViewEvent {
    data object initLoadingHistory : FlupetHistoryViewEvent()

    data object OnFinishToast : FlupetHistoryViewEvent()
}
