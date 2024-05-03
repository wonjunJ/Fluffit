package com.kiwa.ranking

import com.kiwa.fluffit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor() : BaseViewModel<RankingViewState, RankingViewEvent>() {
    override fun createInitialState(): RankingViewState =
        RankingViewState.AgeRanking(ageRankingList = emptyList())

    override fun onTriggerEvent(event: RankingViewEvent) {
        when (event) {
            RankingViewEvent.OnClickAgeRankingButton -> setState { showAgeRanking() }
            RankingViewEvent.OnClickBattleRankingButton -> setState { showBattleRanking() }
        }
    }

    private fun RankingViewState.showAgeRanking(): RankingViewState =
        when (this) {
            is RankingViewState.AgeRanking -> this
            is RankingViewState.BattleRanking ->
                RankingViewState.AgeRanking(ageRankingList = emptyList())
        }

    private fun RankingViewState.showBattleRanking(): RankingViewState =
        when (this) {
            is RankingViewState.AgeRanking ->
                RankingViewState.BattleRanking(battleRankingList = emptyList())

            is RankingViewState.BattleRanking -> this
        }
}
