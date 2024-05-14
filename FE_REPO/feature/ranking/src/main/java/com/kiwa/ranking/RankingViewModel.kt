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

    private suspend fun getBattleRanking() {
        getBattleRankingUseCase().fold(
            onSuccess = { setState { showBattleRanking(it) } },
            onFailure = {}
        )
    }

    private suspend fun getAgeRanking() {
        getAgeRankingUseCase().fold(
            onSuccess = {
                setState { showAgeRanking(it) }
            },
            onFailure = {}
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
