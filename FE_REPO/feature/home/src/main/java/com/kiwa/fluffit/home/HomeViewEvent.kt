package com.kiwa.fluffit.home

import com.kiwa.fluffit.base.ViewEvent

sealed class HomeViewEvent : ViewEvent {
    data object OnClickPencilButton : HomeViewEvent()

    data class OnClickConfirmEditButton(val name: String) : HomeViewEvent()

    data class OnUpdateFullness(val stat: String = "fullness") : HomeViewEvent()

    data class OnUpdateHealth(val stat: String = "health") : HomeViewEvent()

    data object OnClickTombStone : HomeViewEvent()

    data object OnClickNewEggButton : HomeViewEvent()

    data object OnDismissSnackBar : HomeViewEvent()

    data object OnClickEvolutionButton : HomeViewEvent()
}
