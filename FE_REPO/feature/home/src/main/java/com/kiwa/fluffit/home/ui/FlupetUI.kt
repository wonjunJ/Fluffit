package com.kiwa.fluffit.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.size.Size
import com.kiwa.fluffit.home.HomeViewState
import com.kiwa.fluffit.home.components.FlupetAndStatUI

@Composable
internal fun FlupetUI(
    uiState: HomeViewState,
    modifier: Modifier,
    onUpdateFullness: () -> Unit,
    onUpdateHealth: () -> Unit,
    imageLoader: ImageLoader,
    originalSize: Size,
    onClickPencilButton: () -> Unit,
    onClickConfirmButton: (String) -> Unit
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        FlupetAndStatUI(
            uiState = uiState,
            onUpdateFullness = onUpdateFullness,
            onUpdateHealth = onUpdateHealth
        )
        Image(
            painter = rememberImagePainter(
                imageLoader = imageLoader,
                data = uiState.flupet.imageUrls.standard,
                builder = {
                    size(originalSize)
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(top = 32.dp)
        )
        FlupetNameUI(uiState, onClickPencilButton, onClickConfirmButton)
    }
}
