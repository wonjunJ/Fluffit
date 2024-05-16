package com.kiwa.fluffit.presentation.battle

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.battle.BattleLogModel
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.presentation.battle.usecase.FindMatchingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
//    private val getBattleLogUseCase: GetBattleLogsUseCase,
//    private val getBattleStatisticsUseCase: GetBattleStatisticsUseCase,
    private val findMatchingUseCase: FindMatchingUseCase,
) : BaseViewModel<BattleViewState, BattleViewEvent>() {
    override fun createInitialState(): BattleViewState = BattleViewState.Default()

    override fun onTriggerEvent(event: BattleViewEvent) {
        setEvent(event)
    }

    init {
        handleUIEvent()
//        getBattleLog()
    }

    private fun handleUIEvent() {
        viewModelScope.launch {
            uiEvent.collect { event ->
                when (event) {
                    BattleViewEvent.OnClickCancelBattleButton -> setState { cancelFindMatching() }
                    BattleViewEvent.OnClickBattleButton -> findMatching()
                    BattleViewEvent.OnDismissToast -> setState { onDismissToast() }
                }
            }
        }
    }

    private fun BattleViewState.onDismissToast(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(message = "")
        }

    private suspend fun findMatching() {
        setState { setLoading() }
        findMatchingUseCase().fold(
            onSuccess = { setState { findMatchingCompleted(it) } },
            onFailure = { setState { cancelFindMatching("매칭에 실패했습니다.") } }
        )
    }

    private fun BattleViewState.findMatchingCompleted(gameUIModel: GameUIModel): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(
                gameUIModel = gameUIModel,
                findMatching = false,
                loading = false,
            )
        }


    private fun BattleViewState.cancelFindMatching(message: String = ""): BattleViewState {
        return when (this) {
            is BattleViewState.Default -> this.copy(
                loading = false,
                findMatching = false,
                message = message
            )
        }
    }

    private fun BattleViewState.setLoading(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(loading = true, findMatching = true)
        }


//    private fun getBattleLog() {
//        viewModelScope.launch {
//            getBattleStatisticsUseCase().fold(
//                onSuccess = { setState { updateBattleStatistics(battleUIModel = it) } },
//                onFailure = { }
//            )
//        }
//    }

    private fun BattleViewState.updateBattleStatistics(battleLogModel: BattleLogModel) =
        BattleViewState.Default(battleLogModel = battleLogModel)

}