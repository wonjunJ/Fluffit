package com.kiwa.fluffit.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.kiwa.fluffit.home.HomeViewState

@Composable
internal fun FlupetAndStatUI(
    uiState: HomeViewState,
    onUpdateFullness: () -> Unit,
    onUpdateHealth: () -> Unit
) {
    LaunchedEffect(uiState.nextFullnessUpdateTime) {
        onUpdateFullness()
    }

    LaunchedEffect(uiState.nextHealthUpdateTime) {
        onUpdateHealth()
    }

    FullnessDisplay(stat = uiState.flupet.fullness)
    HealthDisplay(stat = uiState.flupet.health)
}
