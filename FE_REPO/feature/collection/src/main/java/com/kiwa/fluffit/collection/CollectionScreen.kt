package com.kiwa.fluffit.collection

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel<CollectionViewModel>()
) {
    val viewState = viewModel.uiState.collectAsState().value

    if (viewState.isLoadingCollected) {
        viewModel.onTriggerEvent(CollectionViewEvent.initLoadingCollections)
    }

    val snackBarHostState = remember { SnackbarHostState() }

    ObserveToastMessage(viewState = viewState, snackBarHostState, viewModel = viewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (viewState) {
            is CollectionViewState.Init -> CollectionLoadingView()
            is CollectionViewState.Default -> CollectionView(viewState.collectionList)
        }

        CollectionSnackBarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            snackBarHostState = snackBarHostState
        )
    }
}

@Composable
private fun ObserveToastMessage(
    viewState: CollectionViewState,
    snackBarHostState: SnackbarHostState,
    viewModel: CollectionViewModel
) {
    LaunchedEffect(key1 = viewState.toastMessage) {
        if (viewState.toastMessage.isNotEmpty()) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                viewState.toastMessage,
                actionLabel = "확인",
                duration = SnackbarDuration.Short
            )
            viewModel.onTriggerEvent(CollectionViewEvent.OnFinishToast)
        }
    }
}
