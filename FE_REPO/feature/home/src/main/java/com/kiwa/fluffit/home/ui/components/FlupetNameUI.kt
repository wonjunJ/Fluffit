package com.kiwa.fluffit.home.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun FlupetNameUI(
    viewState: HomeViewState,
    onClickPencilButton: () -> Unit,
    onClickConfirmButton: (String) -> Unit,
    name: String
) {
    Row(
        modifier = Modifier.padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (viewState) {
            is HomeViewState.Default -> DisplayModeUI(name = name) {
                onClickPencilButton()
            }

            is HomeViewState.FlupetNameEdit -> EditModeUI(name) {
                onClickConfirmButton(it)
            }

            is HomeViewState.Loading -> TODO()
        }
    }
}

@Composable
fun DisplayModeUI(name: String, onClickPencilButton: () -> Unit) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyMedium
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
        maxLength = 6
    )

    Text(text = "확인", modifier = Modifier.clickable { onClickConfirmButton(textState.value) })
}
