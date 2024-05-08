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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwa.fluffit.model.flupet.FlupetCollection

@Composable
internal fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel<CollectionViewModel>()
) {
    val viewState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

//    if (viewState.isLoadingCollected) {
//        viewModel.onTriggerEvent(CollectionViewEvent.initLoadingCollections)
//    }

    val snackBarHostState = remember { SnackbarHostState() }

    ObserveToastMessage(viewState = viewState, snackBarHostState, viewModel = viewModel)

    //테스트용 더미 데이터 코드
    val list: MutableList<FlupetCollection> = mutableListOf()

    list.add(
        FlupetCollection(
            species = "하얀 토끼",
            imageUrl = "https://www.gstatic.com/webp/gallery3/1.sm.png",
            tier = 1,
            metBefore = true
        )
    )
    list.add(
        FlupetCollection(
            species = "하얀 토끼",
            imageUrl = "https://www.gstatic.com/webp/gallery3/1.sm.png",
            tier = 2,
            metBefore = false
        )
    )

    list.add(
        FlupetCollection(
            species = "갈색 토끼",
            imageUrl = "https://www.gstatic.com/webp/gallery3/1.sm.png",
            tier = 1,
            metBefore = true
        )
    )
    list.add(
        FlupetCollection(
            species = "갈색 토끼",
            imageUrl = "https://www.gstatic.com/webp/gallery3/1.sm.png",
            tier = 2,
            metBefore = true
        )
    )

    list.add(
        FlupetCollection(
            species = "검정 토끼",
            imageUrl = "https://www.gstatic.com/webp/gallery3/1.sm.png",
            tier = 1,
            metBefore = true
        )
    )
    list.add(
        FlupetCollection(
            species = "검정 토끼",
            imageUrl = "https://www.gstatic.com/webp/gallery3/1.sm.png",
            tier = 2,
            metBefore = false
        )
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        when (viewState) {
//            is CollectionViewState.Init -> CollectionLoadingView()
            is CollectionViewState.Init -> CollectionView(list)
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
