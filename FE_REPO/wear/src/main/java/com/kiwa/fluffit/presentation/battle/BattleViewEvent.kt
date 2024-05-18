package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.base.ViewEvent

sealed class BattleViewEvent: ViewEvent {
    data object OnClickBattleButton: BattleViewEvent()

    data object OnDismissToast: BattleViewEvent()

    data object Init: BattleViewEvent()
}
