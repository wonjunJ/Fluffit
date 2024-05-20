package com.kiwa.fluffit.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwa.fluffit.presentation.battle.BattleViewEvent
import com.kiwa.fluffit.presentation.feed.FeedViewEvent
import com.kiwa.fluffit.presentation.feed.FeedViewModel
import com.kiwa.fluffit.presentation.feed.FeedViewState
import com.kiwa.fluffit.presentation.feed.ui.FeedView

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel<FeedViewModel>()
) {
    val context = LocalContext.current
    val viewState = viewModel.uiState.collectAsState().value

    if (viewState is FeedViewState.Init) {
        viewModel.onTriggerEvent(FeedViewEvent.InitLoadingFood)
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            FeedView(viewState, viewModel)
        }
    }

    LaunchedEffect(viewState.message) {
        if (viewState.message.isNotEmpty()) Toast.makeText(
            context,
            viewState.message,
            Toast.LENGTH_SHORT
        ).show()
        viewModel.onTriggerEvent(FeedViewEvent.OnDismissToast)
    }
}

