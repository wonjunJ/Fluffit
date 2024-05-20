package com.kiwa.fluffit.battle.record

import com.kiwa.fluffit.base.ViewEvent

sealed class BattleRecordViewEvent : ViewEvent {
    data object InitLoadingBattleRecord : BattleRecordViewEvent()

    data object InitLoadingBattleStatistics : BattleRecordViewEvent()

    data object OnFinishToast : BattleRecordViewEvent()
}
