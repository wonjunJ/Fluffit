package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.battle.BattleButton

@Composable
fun BattleScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(),
            progress = 0.6f,
            startAngle = -211f,
            endAngle = 35f,
            indicatorColor = colorResource(id = R.color.watchBlue),
            trackColor = colorResource(id = R.color.watchRed),
            strokeWidth = 6.dp
        )

        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "10승 8패", style = MaterialTheme.typography.button)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "1,800", style = MaterialTheme.typography.button)
        }
        BattleButton {}
    }
}
