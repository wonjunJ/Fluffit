package com.kiwa.fluffit.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
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
import com.kiwa.fluffit.home.HomeViewState
import com.kiwa.fluffit.home.R
import com.kiwa.fluffit.home.components.CustomTextField

@Composable
fun FlupetNameUI(
    uiState: HomeViewState,
    onClickPencilButton: () -> Unit,
    onClickConfirmButton: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .padding(start = 32.dp),
        verticalAlignment = Alignment.CenterVertically
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

@Composable
fun DisplayModeUI(name: String, onClickPencilButton: () -> Unit) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.wrapContentWidth(),
        maxLines = 1
    )
    Image(
        painter = painterResource(id = R.drawable.pencil),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp)
            .padding(start = 4.dp)
            .clickable { onClickPencilButton() }
    )
}

@Composable
fun EditModeUI(name: String, onClickConfirmButton: (String) -> Unit) {
    val textState = remember { mutableStateOf(name) }
    CustomTextField(
        textState = textState,
        isSingleLine = true,
        maxLength = 6,
        modifier = Modifier.wrapContentWidth()
    )

    Image(
        painter = painterResource(id = R.drawable.check),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp)
            .padding(start = 4.dp)
            .clickable { onClickConfirmButton(textState.value) }
    )
}
