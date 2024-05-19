package com.kiwa.fluffit.battle.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.designsystem.theme.fluffitTypography
import com.kiwa.fluffit.model.BattleContents

@Composable
fun UserInfoView(
    battleLog: BattleContents,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(2f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "ME", style = fluffitTypography.bodyMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = battleLog.myScore.toString() + "점",
                    style = fluffitTypography.bodyMedium
                )
            }
        }
        Box(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.weight(2f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = battleLog.opponentName, style = fluffitTypography.bodyMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = battleLog.opponentScore.toString() + "점",
                    style = fluffitTypography.bodyMedium
                )
            }
        }

    }
}
