package com.kiwa.fluffit.home

import com.kiwa.fluffit.base.ViewEvent

sealed class HomeViewEvent : ViewEvent {
    data object OnClickPencilButton : HomeViewEvent()

    data class OnClickConfirmEditButton(val name: String) : HomeViewEvent()

    data object OnUpdateFullness : HomeViewEvent()

    data object OnUpdateHealth : HomeViewEvent()

    data object OnClickCollectionButton : HomeViewEvent()

    data object OnClickUserButton : HomeViewEvent()
}
