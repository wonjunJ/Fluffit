package com.kiwa.fluffit.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun HomeRoute(viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()) {
    val uiState: HomeViewState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(uiState = uiState)
}

@Composable
internal fun HomeScreen(
    uiState: HomeViewState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 120.dp)) {
            Row(modifier = Modifier.align(Alignment.TopCenter), verticalAlignment = Alignment.Bottom) {
                CoinDisplay(coin = 1000)
                Spacer(modifier = Modifier.weight(1f))
                RankingButton()
            }
            CollectionButton(modifier = Modifier.align(Alignment.BottomStart))
            UserButton(modifier = Modifier.align(Alignment.BottomEnd))
        }

    }
}

@Composable
internal fun CoinDisplay(coin: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.coin),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Text(text = coin.toString(), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
internal fun CollectionButton(modifier: Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.collection),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
internal fun UserButton(modifier: Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
internal fun RankingButton() {
    Box {
        Image(
            painter = painterResource(id = R.drawable.ranking),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}