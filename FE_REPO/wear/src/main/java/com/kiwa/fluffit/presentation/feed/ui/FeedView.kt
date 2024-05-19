package com.kiwa.fluffit.presentation.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.AnchorType
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.curvedComposable
import androidx.wear.compose.foundation.curvedRow
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.components.CoinDisplay
import com.kiwa.fluffit.presentation.feed.FeedViewModel
import com.kiwa.fluffit.presentation.feed.FeedViewState
import com.kiwa.fluffit.presentation.home.HomeViewModel

@Composable
fun FeedView(
    viewState: FeedViewState,
    viewModel: FeedViewModel,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    val coin by homeViewModel.coin.collectAsState()

    val image1 = painterResource(R.drawable.normal_food)
    val image2 = painterResource(R.drawable.instant_food)
    val image3 = painterResource(R.drawable.healthy_food)
    if (viewState.foodList.isNotEmpty()) {
        CurvedLayout(
            anchorType = AnchorType.Center,
            modifier = Modifier.fillMaxSize(),
            anchor = 270f,
        ) {
            curvedRow() {
                curvedComposable {
                    FeedToggleButton(feedImage = image1, buttonId = 0, viewModel)
                }
                curvedComposable {
                    FeedToggleButton(feedImage = image2, buttonId = 1, viewModel)
                }
                curvedComposable {
                    FeedToggleButton(feedImage = image3, buttonId = 2, viewModel)
                }
            }
        }
        Box(contentAlignment = Alignment.Center) {
            FeedDisplay(viewState, viewModel)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.height(0.dp))
                Box(modifier = Modifier.padding(top = 5.dp)) {
                    CoinDisplay(coin = coin)
                }
            }
        }
        FeedButton(viewState, viewModel, homeViewModel)
        FeedDescriptionBox(viewState, viewModel)
    }
}
