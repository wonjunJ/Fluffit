package com.kiwa.fluffit.presentation.battle

import com.kiwa.fluffit.base.ViewEvent

sealed class BattleViewEvent: ViewEvent {
    data object OnClickBattleButton: BattleViewEvent()

    data object OnClickCancelBattleButton: BattleViewEvent()

    data object OnReadyForBattle: BattleViewEvent()

    data class OnFinishBattle(val battleId: String, val score: Int): BattleViewEvent()

    data object OnConfirmResult: BattleViewEvent()
}
