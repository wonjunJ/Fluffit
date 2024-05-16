package com.kiwa.ranking

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiwa.fluffit.designsystem.ui.FluffitSnackBar

@Composable
internal fun RankingSnackBarHost(modifier: Modifier, snackBarHostState: SnackbarHostState) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackBarHostState
    ) { data ->
        FluffitSnackBar(data)
    }
}
