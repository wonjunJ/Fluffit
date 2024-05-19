package com.kiwa.fluffit.battle.record

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.designsystem.theme.fluffitTypography
import com.kiwa.fluffit.model.BattleContents

@Composable
fun BattleHistoryLog(
    battleLog: BattleContents
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp))
            .clip(shape = RoundedCornerShape(8.dp))
            .background(Color(0xE6FFFFFF))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = battleLog.title,
                style = fluffitTypography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "VS",
                style = fluffitTypography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = dateFormatter(battleLog.date),
                style = fluffitTypography.bodyMedium,
                textAlign = TextAlign.Center
            )
            CrownItemView(
                battleLog.win,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            UserInfoView(
                battleLog,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            )
        }
    }
}

fun dateFormatter(dateString: String): String =
    dateString.substring(0, 4) + "." + dateString.substring(5, 7) + "." + dateString.substring(
        8,
        10
    )
