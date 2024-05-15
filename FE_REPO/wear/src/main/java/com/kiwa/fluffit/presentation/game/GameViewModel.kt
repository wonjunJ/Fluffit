package com.kiwa.fluffit.presentation.game

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.battle.BattleResultUIModel
import com.kiwa.fluffit.model.battle.GameUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor() : BaseViewModel<GameViewState, GameViewEvent>() {
    override fun createInitialState(): GameViewState = GameViewState.MatchingCompleted()

    override fun onTriggerEvent(event: GameViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect {
                when (it) {
                    GameViewEvent.OnReadyForGame -> setState { showGame() }
                    GameViewEvent.OnFinishBattle -> TODO()
                    is GameViewEvent.OnFinishGame -> getBattleResult()
                    is GameViewEvent.Init -> setState { initUI(it.gameUIModel) }
                }
            }
        }
    }

    private fun initUI(gameUIModel: GameUIModel): GameViewState =
        GameViewState.MatchingCompleted(false, gameUIModel = gameUIModel)

    private fun getBattleResult() {
        setState { showBattleResult(BattleResultUIModel(true, "1234", "3424", 3242)) }
    }

    private fun GameViewState.showBattleResult(battleResultUIModel: BattleResultUIModel): GameViewState =
        when (this) {
            is GameViewState.Battle -> GameViewState.BattleResult(
                result = battleResultUIModel,
                loading = false,
                gameUIModel = this.gameUIModel.copy()
            )

            is GameViewState.BattleResult -> this
            is GameViewState.MatchingCompleted -> this
        }

    private fun GameViewState.showGame(): GameViewState =
        when (this) {
            is GameViewState.MatchingCompleted -> GameViewState.Battle(
                score = 0,
                loading = false,
                gameUIModel = this.gameUIModel.copy()
            )

            is GameViewState.Battle -> this
            is GameViewState.BattleResult -> this
        }

}
