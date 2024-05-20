package com.kiwa.fluffit.presentation.battle.ui

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
import com.kiwa.fluffit.model.battle.BattleStatisticsUIModel
import com.kiwa.fluffit.presentation.components.BasicButton

@Composable
internal fun DefaultUI(
    modifier: Modifier,
    mainButtonText: String,
    battleStatistics: BattleStatisticsUIModel,
    onClickMainButton: () -> Unit
) {
    val winRate = battleStatistics.totalWinRate.toFloat()
    val battleLog = "${battleStatistics.totalWin}승 ${battleStatistics.totalLose}패"
    val battlePoint = "${battleStatistics.battlePoint}"

    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize(),
        progress = winRate,
        startAngle = -211f,
        endAngle = 35f,
        indicatorColor = colorResource(id = R.color.watchBlue),
        trackColor = colorResource(id = R.color.watchRed),
        strokeWidth = 6.dp
    )

    Column(
        modifier = Modifier
            .wrapContentSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = battleLog, style = MaterialTheme.typography.body2)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = battlePoint, style = MaterialTheme.typography.body2)
    }

    BasicButton(mainButtonText) { onClickMainButton() }
}
