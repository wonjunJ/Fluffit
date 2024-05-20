package com.kiwa.fluffit.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.designsystem.theme.fluffitTypography

@Composable
internal fun FluffitSnackBarHost(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackBarHostState
    ) { data ->
        Snackbar(
            containerColor = Color.White,
            action = {
                data.visuals.actionLabel?.let { actionLabel ->
                    TextButton(onClick = {
                        data.dismiss()
                    }) {
                        Text(actionLabel)
                    }
                }
            },
            content = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.rabbit_white),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = data.visuals.message,
                        style = fluffitTypography.bodySmall
                    )
                }
            }
        )
    }
}
