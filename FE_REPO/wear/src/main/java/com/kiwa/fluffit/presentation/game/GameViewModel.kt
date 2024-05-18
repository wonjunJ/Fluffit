package com.kiwa.fluffit.presentation.game

import androidx.lifecycle.viewModelScope
import com.kiwa.fluffit.presentation.battle.usecase.GetBattleResultUseCase
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.battle.BattleResultUIModel
import com.kiwa.fluffit.model.battle.GameUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getBattleResultUseCase: GetBattleResultUseCase,
) : BaseViewModel<GameViewState, GameViewEvent>() {
    override fun createInitialState(): GameViewState = GameViewState.MatchingCompleted()

    override fun onTriggerEvent(event: GameViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect {
                when (it) {
                    GameViewEvent.OnReadyForGame -> setState { showGame() }
                    is GameViewEvent.OnFinishGame -> getBattleResult(it.battleId, it.score)
                    is GameViewEvent.Init -> setState { initUI(it.gameUIModel) }
                }
            }
        }
    }

    private fun initUI(gameUIModel: GameUIModel): GameViewState =
        GameViewState.MatchingCompleted(false, gameUIModel = gameUIModel)

    private suspend fun getBattleResult(battleId: String, score: Int) {
        getBattleResultUseCase(battleId, score).fold(
            onSuccess = { result -> setState { showBattleResult(result) } },
            onFailure = { }
        )
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
