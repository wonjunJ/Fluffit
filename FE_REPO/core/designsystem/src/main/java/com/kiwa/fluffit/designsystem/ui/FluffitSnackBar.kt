package com.kiwa.fluffit.designsystem.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
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
fun FluffitSnackBar(data: SnackbarData, @DrawableRes image: Int) {
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
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp),
                    painter = painterResource(id = image),
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
