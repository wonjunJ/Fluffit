package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.CircularProgressIndicator
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.components.CoinDisplay
import com.kiwa.fluffit.presentation.components.HomePetImageDisplay
import com.kiwa.fluffit.presentation.components.StepsDisplay
import com.kiwa.fluffit.presentation.home.HomeViewModel

@Composable
fun MainScreen() {
    val homeViewModel:HomeViewModel = hiltViewModel()
    homeViewModel.loadFlupetStatus()

    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f),
            progress = 0.5f,
            startAngle = -88f,
            endAngle = 35f,
            indicatorColor = colorResource(id = R.color.watchGreen),
            trackColor = colorResource(id = R.color.watchDarkGreen),
            strokeWidth = 6.dp
        )

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = 0.5f,
            startAngle = -88f,
            endAngle = 35f,
            indicatorColor = colorResource(id = R.color.watchBlue),
            trackColor = colorResource(id = R.color.watchDarkBlue),
            strokeWidth = 6.dp
        )

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            StepsDisplay()
        }
        Box(modifier = Modifier.align(Alignment.Center)){
            val image = painterResource(R.drawable.dog_white)
//        Spacer(modifier = Modifier.height(16.dp))
            HomePetImageDisplay(image)
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            CoinDisplay(coin = 13420)
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}
