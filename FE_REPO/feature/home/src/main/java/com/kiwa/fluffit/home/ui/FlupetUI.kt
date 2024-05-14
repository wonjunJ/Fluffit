package com.kiwa.fluffit.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.kiwa.fluffit.home.HomeViewState
import com.kiwa.fluffit.home.R
import com.kiwa.fluffit.home.components.FlupetAndStatUI

@Composable
internal fun FlupetUI(
    uiState: HomeViewState,
    modifier: Modifier,
    onUpdateFullness: () -> Unit,
    onUpdateHealth: () -> Unit,
    imageLoader: ImageLoader,
    onClickPencilButton: () -> Unit,
    onClickConfirmButton: (String) -> Unit,
    onClickEvolutionButton: () -> Unit
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
            painter = rememberAsyncImagePainter(
                imageLoader = imageLoader,
                model = uiState.flupet.imageUrls.standard
            ),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(top = 32.dp)
        )
        FlupetNameUI(uiState, imageLoader, onClickPencilButton, onClickConfirmButton)

        if (uiState.flupet.evolutionAvailable) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.wrapContentSize().clickable { onClickEvolutionButton() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.evolution_button),
                    contentDescription = null,
                    modifier = Modifier.width(64.dp)
                )
                Text(text = "진화하기", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
