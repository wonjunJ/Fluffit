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
                    BattleViewEvent.OnClickBattleButton -> setState { findMatchingCompleted() }
                    BattleViewEvent.OnConfirmResult -> TODO()
                    is BattleViewEvent.OnFinishBattle -> TODO()
                    BattleViewEvent.OnReadyForBattle -> setState { startBattle() }
                }
            }
        }
    }

    private fun BattleViewState.startBattle(): BattleViewState =
        when (this) {
            is BattleViewState.MatchingCompleted -> BattleViewState.Battle(
                battleType = this.battleType,
                score = 0,
                loading = false,
                battleId = this.battleId
            )
            else -> this
        }

    private fun BattleViewState.findMatching(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(loading = true, findMatching = true)
            else -> this
        }

    private fun BattleViewState.findMatchingCompleted(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> BattleViewState.MatchingCompleted(
                battleId = "1",
                loading = false,
                opponentInfo = OpponentInfo(
                    "적입니다", "", "https://github.com/shjung53/algorithm_study/assets/" +
                        "90888718/4399f85d-7810-464c-ad76-caae980ce047", 0
                ),
                battleType = BattleType.BreakStone("", "", ""),
            )

            else -> this
        }


    private fun BattleViewState.cancelFindMatching(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(loading = false, findMatching = false)
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
