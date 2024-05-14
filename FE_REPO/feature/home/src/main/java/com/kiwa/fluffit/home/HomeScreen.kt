package com.kiwa.fluffit.home

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.kiwa.fluffit.home.components.CoinDisplay
import com.kiwa.fluffit.home.components.FlupetImageButton
import com.kiwa.fluffit.home.ui.FlupetUI
import com.kiwa.fluffit.home.ui.NoFlupetUI
import com.kiwa.fluffit.home.ui.TombStoneUI
import com.kiwa.fluffit.model.flupet.FlupetStatus

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    onNavigateToCollection: () -> Unit,
    onNavigateToRankingDialog: () -> Unit,
    onNavigateToMyPage: () -> Unit
) {
    val uiState: HomeViewState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onClickPencilButton = { viewModel.onTriggerEvent(HomeViewEvent.OnClickPencilButton) },
        onClickConfirmButton = { name ->
            viewModel.onTriggerEvent(
                HomeViewEvent.OnClickConfirmEditButton(
                    name
                )
            )
        },
        onClickRankingButton = onNavigateToRankingDialog,
        onClickCollectionButton = onNavigateToCollection,
        onUpdateFullness = { viewModel.onTriggerEvent(HomeViewEvent.OnUpdateFullness()) },
        onUpdateHealth = { viewModel.onTriggerEvent(HomeViewEvent.OnUpdateHealth()) },
        onClickMyPage = onNavigateToMyPage,
        onClickTombStone = { viewModel.onTriggerEvent(HomeViewEvent.OnClickTombStone) },
        onClickEmptyEgg = { viewModel.onTriggerEvent(HomeViewEvent.OnClickNewEggButton) },
        onDismissSnackBar = { viewModel.onTriggerEvent(HomeViewEvent.OnDismissSnackBar) },
        onClickEvolutionButton = { viewModel.onTriggerEvent(HomeViewEvent.OnClickEvolutionButton) }
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeViewState,
    onClickPencilButton: () -> Unit,
    onClickCollectionButton: () -> Unit,
    onClickConfirmButton: (String) -> Unit,
    onClickRankingButton: () -> Unit,
    onUpdateFullness: () -> Unit,
    onUpdateHealth: () -> Unit,
    onClickTombStone: () -> Unit,
    onClickEmptyEgg: () -> Unit,
    onClickMyPage: () -> Unit,
    onDismissSnackBar: () -> Unit,
    onClickEvolutionButton: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()

    val snackBarHostState = remember { SnackbarHostState() }

    ObserveToastMessage(uiState, snackBarHostState) { onDismissSnackBar() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        MainButtons(uiState.coin, onClickCollectionButton, onClickRankingButton, onClickMyPage)

        when (uiState.flupetStatus) {
            FlupetStatus.Alive -> FlupetUI(
                uiState,
                Modifier
                    .align(Alignment.Center),
                onUpdateFullness,
                onUpdateHealth,
                imageLoader,
                onClickPencilButton,
                onClickConfirmButton,
                onClickEvolutionButton
            )

            FlupetStatus.Dead -> TombStoneUI(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { onClickTombStone() },
                name = uiState.flupet.name
            )

            FlupetStatus.None -> NoFlupetUI(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { onClickEmptyEgg() }
            )
        }

        HomeSnackBarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            snackBarHostState = snackBarHostState
        )
    }
}

@Composable
private fun MainButtons(
    coin: Int,
    onClickCollectionButton: () -> Unit,
    onClickRankingButton: () -> Unit,
    onClickMyPage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.TopCenter), verticalAlignment = Alignment.Bottom) {
            CoinDisplay(coin = coin)
            Spacer(modifier = Modifier.weight(1f))
            RankingButton(onClickRankingButton)
        }
        Spacer(modifier = Modifier.height(120.dp))
        CollectionButton(
            modifier = Modifier.align(Alignment.BottomStart),
            onNavigateToCollection = onClickCollectionButton
        )
        UserButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onNavigateToMyPage = onClickMyPage
        )
    }
}

@Composable
private fun CollectionButton(modifier: Modifier, onNavigateToCollection: () -> Unit) {
    FlupetImageButton(id = R.drawable.collection, modifier = modifier, onNavigateToCollection)
}

@Composable
private fun UserButton(modifier: Modifier, onNavigateToMyPage: () -> Unit) {
    FlupetImageButton(id = R.drawable.user, modifier = modifier, onNavigateToMyPage)
}

@Composable
private fun RankingButton(onClickRankingButton: () -> Unit) {
    FlupetImageButton(id = R.drawable.ranking, onClickImage = onClickRankingButton)
}

@Composable
private fun ObserveToastMessage(
    uiState: HomeViewState,
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
