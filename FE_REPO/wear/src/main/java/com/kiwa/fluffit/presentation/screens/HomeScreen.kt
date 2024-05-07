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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.CircularProgressIndicator
import com.example.wearapp.presentation.HealthViewModel
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.components.CoinDisplay
import com.kiwa.fluffit.presentation.components.MainPetImageDisplay
import com.kiwa.fluffit.presentation.components.StepsDisplay

@Composable
fun MainScreen() {
    val healthViewModel : HealthViewModel = hiltViewModel()

    val steps by healthViewModel.steps.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f),
            progress = 0.5f,
            startAngle = -88f,
            endAngle = 35f,
            indicatorColor = Color(0xFF70F150),
            trackColor = Color(0xFF347923),
            strokeWidth = 6.dp
        )

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = 0.5f,
            startAngle = -88f,
            endAngle = 35f,
            indicatorColor = Color(0xFF64B5FF),
            trackColor = Color(0xFF335E85),
            strokeWidth = 6.dp
        )

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            StepsDisplay(steps = steps)
        }
        Box(modifier = Modifier.align(Alignment.Center)){
            val image = painterResource(R.drawable.dog_white)
//        Spacer(modifier = Modifier.height(16.dp))
            MainPetImageDisplay(image)
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            CoinDisplay(coin = 13420)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
