package com.kiwa.ranking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiwa.ranking.ui.RankingUI

@Composable
internal fun RankingRoute(
    viewModel: RankingViewModel = hiltViewModel<RankingViewModel>(),
    onDismissDialog: () -> Unit
) {
    val uiState: RankingViewState by viewModel.uiState.collectAsStateWithLifecycle()
    RankingDialog(
        uiState = uiState,
        onDismissRequest = { onDismissDialog() },
        { viewModel.onTriggerEvent(RankingViewEvent.OnClickAgeRankingButton) },
        { viewModel.onTriggerEvent(RankingViewEvent.OnClickBattleRankingButton) }
    )
}

@Composable
internal fun RankingDialog(
    uiState: RankingViewState,
    onDismissRequest: () -> Unit,
    onClickAgeRankingButton: () -> Unit,
    onClickBattleRankingButton: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        when (uiState) {
            is RankingViewState.AgeRanking -> RankingUI(
                rankingList = uiState.ageRankingList,
                myRanking = uiState.myRanking,
                onClickShowAnotherRankingButton = onClickBattleRankingButton,
                "장수 랭킹",
                "배틀 랭킹 보기"
            )

            is RankingViewState.BattleRanking -> RankingUI(
                rankingList = uiState.battleRankingList,
                myRanking = uiState.myRanking,
                onClickShowAnotherRankingButton = onClickAgeRankingButton,
                "배틀 랭킹 보기",
                "장수 랭킹 보기"
            )
        }
    }
}
