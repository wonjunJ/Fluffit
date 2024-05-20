package com.kiwa.fluffit.battle.record

import com.kiwa.fluffit.base.ViewState
import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics

sealed class BattleRecordViewState : ViewState {
    abstract val isLoadingBattleStats: Boolean
    abstract val isLoadingBattleHistory: Boolean
    abstract val stats: UserBattleStatistics
    abstract val history: UserBattleHistory
    abstract val toastMessage: String

    data class BattleStatsLoading(
        override val isLoadingBattleStats: Boolean = true,
        override val isLoadingBattleHistory: Boolean = false,
        override val stats: UserBattleStatistics = UserBattleStatistics(
            battlePoint = -1,
            battleStatisticItemDtoList = emptyList()
        ),
        override val history: UserBattleHistory = UserBattleHistory(
            content = emptyList(),
            hasNext = false
        ),
        override val toastMessage: String = ""
    ) : BattleRecordViewState()

    data class BattleHistoryLoading(
        override val isLoadingBattleStats: Boolean = false,
        override val isLoadingBattleHistory: Boolean = true,
        override val stats: UserBattleStatistics = UserBattleStatistics(
            battlePoint = -1,
            battleStatisticItemDtoList = emptyList()
        ),
        override val history: UserBattleHistory = UserBattleHistory(
            content = emptyList(),
            hasNext = false
        ),
        override val toastMessage: String = ""
    ) : BattleRecordViewState()

    data class Default(
        override val isLoadingBattleStats: Boolean = false,
        override val isLoadingBattleHistory: Boolean = false,
        override val stats: UserBattleStatistics = UserBattleStatistics(
            battlePoint = -1,
            battleStatisticItemDtoList = emptyList()
        ),
        override val history: UserBattleHistory = UserBattleHistory(
            content = emptyList(),
            hasNext = false
        ),
        override val toastMessage: String = ""
    ) : BattleRecordViewState()
}
