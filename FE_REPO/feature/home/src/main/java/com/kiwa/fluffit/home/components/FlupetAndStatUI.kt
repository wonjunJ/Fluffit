package com.kiwa.fluffit.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.work.WorkManager
import com.kiwa.fluffit.home.HomeViewState
import com.kiwa.fluffit.home.enqueueNewRequest

@Composable
internal fun FlupetAndStatUI(uiState: HomeViewState) {
    val context = LocalContext.current

    val workManager = WorkManager.getInstance(context)

    LaunchedEffect(uiState.nextFullnessUpdateTime) {
        workManager.enqueueNewRequest("fullness", uiState.nextFullnessUpdateTime)
    }

    LaunchedEffect(uiState.nextHealthUpdateTime) {
        workManager.enqueueNewRequest("health", uiState.nextHealthUpdateTime)
    }

    FullnessDisplay(stat = uiState.flupet.fullness)
    HealthDisplay(stat = uiState.flupet.health)
}
