package com.kiwa.ranking

import androidx.lifecycle.viewModelScope
import com.kiwa.domain.usecase.GetAgeRankingUseCase
import com.kiwa.domain.usecase.GetBattleRankingUseCase
import com.kiwa.fluffit.base.BaseViewModel
import com.kiwa.fluffit.model.ranking.RankingUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val getBattleRankingUseCase: GetBattleRankingUseCase,
    private val getAgeRankingUseCase: GetAgeRankingUseCase
) : BaseViewModel<RankingViewState, RankingViewEvent>() {
    override fun createInitialState(): RankingViewState =
        RankingViewState.AgeRanking()

    override fun onTriggerEvent(event: RankingViewEvent) {
        setEvent(event)
    }

    init {
        viewModelScope.launch {
            uiEvent.collect {
                when (it) {
                    RankingViewEvent.OnClickAgeRankingButton -> getAgeRanking()
                    RankingViewEvent.OnClickBattleRankingButton -> getBattleRanking()
                    RankingViewEvent.OnDismissSnackBar -> setState { resetMessage() }
                }
            }
        }
        viewModelScope.launch {
            when (currentState) {
                is RankingViewState.AgeRanking -> getAgeRanking()
                is RankingViewState.BattleRanking -> getBattleRanking()
            }
        }
    }

    private fun RankingViewState.onUpdateMessage(message: String): RankingViewState =
        when (this) {
            is RankingViewState.AgeRanking -> this.copy(message = message)
            is RankingViewState.BattleRanking -> this.copy(message = message)
        }

    private fun RankingViewState.resetMessage(): RankingViewState =
        when (this) {
            is RankingViewState.AgeRanking -> this.copy(message = "")
            is RankingViewState.BattleRanking -> this.copy(message = "")
        }

    private suspend fun getBattleRanking() {
        getBattleRankingUseCase().fold(
            onSuccess = { setState { showBattleRanking(it) } },
            onFailure = { setState { onUpdateMessage(it.message.toString()) } }
        )
    }

    private suspend fun getAgeRanking() {
        getAgeRankingUseCase().fold(
            onSuccess = {
                setState { showAgeRanking(it) }
            },
            onFailure = { setState { onUpdateMessage(it.message.toString()) } }
        )
    }

    private fun showAgeRanking(rankingUIModel: RankingUIModel): RankingViewState =
        RankingViewState.AgeRanking(
            ageRankingList = rankingUIModel.rankingList,
            myRanking = rankingUIModel.myRanking
        )

    private fun showBattleRanking(rankingUIModel: RankingUIModel): RankingViewState =
        RankingViewState.BattleRanking(
            battleRankingList = rankingUIModel.rankingList,
            myRanking = rankingUIModel.myRanking
        )
}
