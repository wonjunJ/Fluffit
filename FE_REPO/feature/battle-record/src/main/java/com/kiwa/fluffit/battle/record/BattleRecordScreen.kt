package com.kiwa.fluffit.battle.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BattleRecordScreen(
    viewModel: BattleRecordViewModel = hiltViewModel<BattleRecordViewModel>()
) {
    val viewState = viewModel.uiState.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }

    if(viewState.isLoadingBattleStats){
        viewModel.onTriggerEvent(BattleRecordViewEvent.InitLoadingBattleStatistics)
    }
    else if (viewState.isLoadingBattleHistory) {
        viewModel.onTriggerEvent(BattleRecordViewEvent.InitLoadingBattleRecord)
    }

    ObserveToastMessage(
        viewState = viewState,
        snackBarHostState,
        viewModel = viewModel
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (viewState) {
            is BattleRecordViewState.BattleStatsLoading -> BattleRecordLoadingView()
            is BattleRecordViewState.BattleHistoryLoading -> BattleRecordLoadingView()
            is BattleRecordViewState.Default -> BattleRecordView(viewState.stats,viewState.history)
        }

        BattleRecordSnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            snackBarHostState = snackBarHostState
        )
    }
}

@Composable
private fun ObserveToastMessage(
    viewState: BattleRecordViewState,
    snackBarHostState: SnackbarHostState,
    viewModel: BattleRecordViewModel
) {
    LaunchedEffect(key1 = viewState.toastMessage) {
        if (viewState.toastMessage.isNotEmpty()) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                viewState.toastMessage,
                actionLabel = "확인",
                duration = SnackbarDuration.Short
            )
            viewModel.onTriggerEvent(BattleRecordViewEvent.OnFinishToast)
        }
    }
}
