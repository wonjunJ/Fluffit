package com.kiwa.fluffit.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.kiwa.fluffit.home.HomeViewState
import com.kiwa.fluffit.home.R
import com.kiwa.fluffit.home.components.CustomTextField

@Composable
fun FlupetNameUI(
    uiState: HomeViewState,
    imageLoader: ImageLoader,
    onClickPencilButton: () -> Unit,
    onClickConfirmButton: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .padding(top = 12.dp)
    ) {
        if (uiState.flupet.evolutionAvailable) {
            Image(
                painter = rememberAsyncImagePainter(
                    imageLoader = imageLoader,
                    model = R.raw.evolution
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterStart)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 40.dp)
        ) {
            when (uiState) {
                is HomeViewState.FlupetNameEdit -> EditModeUI(uiState.flupet.name) {
                    onClickConfirmButton(it)
                }

                else -> DisplayModeUI(name = uiState.flupet.name) {
                    onClickPencilButton()
                }
            }
        }
    }
}

@Composable
fun DisplayModeUI(name: String, onClickPencilButton: () -> Unit) {
    Box(modifier = Modifier.wrapContentWidth()) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(end = 40.dp),
            maxLines = 1
        )
        Image(
            painter = painterResource(id = R.drawable.pencil),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .clickable { onClickPencilButton() }
        )
    }
}

@Composable
fun EditModeUI(name: String, onClickConfirmButton: (String) -> Unit) {
    val textState = remember { mutableStateOf(name) }
    Box(modifier = Modifier.wrapContentWidth()) {
        CustomTextField(
            textState = textState,
            isSingleLine = true,
            maxLength = 6,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(end = 40.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.check),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .clickable { onClickConfirmButton(textState.value) }
        )
    }
}
