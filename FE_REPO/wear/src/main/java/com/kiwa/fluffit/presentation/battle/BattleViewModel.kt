package com.kiwa.fluffit.presentation.battle

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.battle.BattleLogModel
import com.kiwa.fluffit.model.battle.OpponentInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
//    private val getBattleLogUseCase: GetBattleLogsUseCase,
//    private val getBattleStatisticsUseCase: GetBattleStatisticsUseCase,
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
                }
            }
        }
    }

    private fun findMatching() {
        setState { setLoading() }
        setState { findMatchingCompleted() }
    }

    private fun BattleViewState.findMatchingCompleted(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(
                opponentInfo = OpponentInfo(
                    "적입니다",
                    "",
                    "https://github.com/shjung53/algorithm_study/assets/" +
                        "90888718/4399f85d-7810-464c-ad76-caae980ce047",
                    0
                ),
                loading = false,
            )
        }


    private fun BattleViewState.cancelFindMatching(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(loading = false, findMatching = false)
            else -> this
        }

    private fun BattleViewState.setLoading(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(loading = true)
            else -> this
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
