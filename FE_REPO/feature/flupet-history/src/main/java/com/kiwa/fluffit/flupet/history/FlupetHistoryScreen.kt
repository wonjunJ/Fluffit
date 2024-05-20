package com.kiwa.fluffit.flupet.history

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
internal fun FlupetHistoryScreen(
    viewModel: FlupetHistoryViewModel = hiltViewModel<FlupetHistoryViewModel>()
) {
    val viewState = viewModel.uiState.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }

    if (viewState.isLoadingCollected) {
        viewModel.onTriggerEvent(FlupetHistoryViewEvent.initLoadingHistory)
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
            is FlupetHistoryViewState.Init -> FlupetHistoryLoadingView()
            is FlupetHistoryViewState.Default -> FlupetHistoryView(viewState.flupetHistoryList)
        }

        FlupetHistorySnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            snackBarHostState = snackBarHostState
        )
    }
}

@Composable
private fun ObserveToastMessage(
    viewState: FlupetHistoryViewState,
    snackBarHostState: SnackbarHostState,
    viewModel: FlupetHistoryViewModel
) {
    LaunchedEffect(key1 = viewState.toastMessage) {
        if (viewState.toastMessage.isNotEmpty()) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                viewState.toastMessage,
                actionLabel = "확인",
                duration = SnackbarDuration.Short
            )
            viewModel.onTriggerEvent(FlupetHistoryViewEvent.OnFinishToast)
        }
    }
}
