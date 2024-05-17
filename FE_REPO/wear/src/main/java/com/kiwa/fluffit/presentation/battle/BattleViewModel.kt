package com.kiwa.fluffit.presentation.battle

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.battle.BattleStatisticsUIModel
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.presentation.battle.usecase.CancelFindMatchingUseCase
import com.kiwa.fluffit.presentation.battle.usecase.FindMatchingUseCase
import com.kiwa.fluffit.presentation.battle.usecase.GetBattleStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
    private val getBattleStatisticsUseCase: GetBattleStatisticsUseCase,
    private val findMatchingUseCase: FindMatchingUseCase,
    private val cancelFindMatchingUseCase: CancelFindMatchingUseCase
) : BaseViewModel<BattleViewState, BattleViewEvent>() {
    override fun createInitialState(): BattleViewState = BattleViewState.Default()

    override fun onTriggerEvent(event: BattleViewEvent) {
        setEvent(event)
    }

    init {
        handleUIEvent()
    }

    private fun handleUIEvent() {
        viewModelScope.launch {
            uiEvent.collect { event ->
                when (event) {
                    BattleViewEvent.OnClickCancelBattleButton -> cancelFindMatching()
                    BattleViewEvent.OnClickBattleButton -> findMatching()
                    BattleViewEvent.OnDismissToast -> setState { onDismissToast() }
                    BattleViewEvent.Init -> getBattleStatistics()
                }
            }
        }
    }

    private suspend fun cancelFindMatching() = cancelFindMatchingUseCase().fold(
        onSuccess = { setState { findMatchingCanceled() } },
        onFailure = { setState { onFailure(it.message.toString()) } }
    )

    private fun BattleViewState.onFailure(message: String): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(message = message)
        }

    private fun BattleViewState.onDismissToast(): BattleViewState =
        when (this) {
            is BattleViewState.Default -> this.copy(message = "")
        }

    private suspend fun findMatching() {
        setState { setLoading() }
        findMatchingUseCase().fold(
            onSuccess = { setState { findMatchingCompleted(it) } },
            onFailure = { setState { findMatchingCanceled(it.message.toString()) } }
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


    private fun BattleViewState.findMatchingCanceled(message: String = ""): BattleViewState {
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


    private suspend fun getBattleStatistics() {
        getBattleStatisticsUseCase().fold(
            onSuccess = { setState { updateBattleStatistics(it) } },
            onFailure = { }
        )
    }

    private fun BattleViewState.updateBattleStatistics(battleStatisticsUIModel: BattleStatisticsUIModel) =
        BattleViewState.Default(battleStatistics = battleStatisticsUIModel)
}
