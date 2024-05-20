package com.kiwa.fluffit.battle.record

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.LoadUserBattleRecordUseCase
import com.kiwa.domain.usecase.LoadUserBattleStatisticsUseCase
import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleRecordViewModel @Inject constructor(
    private val loadUserBattleRecordUseCase: LoadUserBattleRecordUseCase,
    private val loadUserBattleStatisticsUseCase: LoadUserBattleStatisticsUseCase
) : BaseViewModel<BattleRecordViewState, BattleRecordViewEvent>() {
    override fun createInitialState(): BattleRecordViewState =
        BattleRecordViewState.BattleStatsLoading()

    override fun onTriggerEvent(event: BattleRecordViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect { event ->
                battleRecordViewEvent(event)
            }
        }
    }

    private fun battleRecordViewEvent(event: BattleRecordViewEvent) {
        when (event) {
            BattleRecordViewEvent.InitLoadingBattleStatistics -> tryLoadBattleStats()
            BattleRecordViewEvent.InitLoadingBattleRecord -> tryLoadBattleRecord()
            BattleRecordViewEvent.OnFinishToast -> setState { onFinishToast() }
        }
    }

    private fun tryLoadBattleStats() {
        viewModelScope.launch {
            loadUserBattleStatisticsUseCase().fold(
                onSuccess = { stats ->
                    setState {
                        BattleRecordViewState.BattleHistoryLoading(
                            stats = stats,
                            history = this.history
                        )
                    }
                },
                onFailure = {
                    setState { showToast("배틀 통계 조회 실패") }
                }
            )
        }
    }

    private fun tryLoadBattleRecord() {
        viewModelScope.launch {
            loadUserBattleRecordUseCase().fold(
                onSuccess = { history ->
                    setState {
                        BattleRecordViewState.Default(
                            stats = this.stats,
                            history = history
                        )
                    }
                },
                onFailure = {
                    setState { showToast("배틀 전적 조회 실패") }
                }
            )
        }
    }

    private fun BattleRecordViewState.showToast(message: String): BattleRecordViewState =
        when (this) {
            is BattleRecordViewState.BattleStatsLoading -> this.copy(toastMessage = message)
            is BattleRecordViewState.BattleHistoryLoading -> this.copy(toastMessage = message)
            is BattleRecordViewState.Default -> this.copy(toastMessage = message)
        }

    private fun BattleRecordViewState.onFinishToast(): BattleRecordViewState =
        when (this) {
            is BattleRecordViewState.BattleStatsLoading -> this.copy(toastMessage = "")
            is BattleRecordViewState.BattleHistoryLoading -> this.copy(toastMessage = "")
            is BattleRecordViewState.Default -> this.copy(toastMessage = "")
        }
}
