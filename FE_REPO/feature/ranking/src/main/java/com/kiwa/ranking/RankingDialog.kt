package com.kiwa.ranking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        { viewModel.onTriggerEvent(RankingViewEvent.OnClickBattleRankingButton) },
        { viewModel.onTriggerEvent(RankingViewEvent.OnDismissSnackBar) }
    )
}

@Composable
internal fun RankingDialog(
    uiState: RankingViewState,
    onDismissRequest: () -> Unit,
    onClickAgeRankingButton: () -> Unit,
    onClickBattleRankingButton: () -> Unit,
    onDismissSnackBar: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveToastMessage(uiState, snackBarHostState) { onDismissSnackBar() }

    Box(modifier = Modifier.fillMaxSize()) {
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
        RankingSnackBarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            snackBarHostState = snackBarHostState
        )
    }
}

@Composable
private fun ObserveToastMessage(
    uiState: RankingViewState,
    snackBarHostState: SnackbarHostState,
    onDismissSnackBar: () -> Unit
) {
    LaunchedEffect(key1 = uiState.message) {
        if (uiState.message.isNotEmpty()) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                uiState.message,
                actionLabel = "확인",
                duration = SnackbarDuration.Short
            )
            onDismissSnackBar()
        }
    }
}
