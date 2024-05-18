package com.kiwa.fluffit.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.presentation.battle.BattleViewEvent
import com.kiwa.fluffit.presentation.battle.BattleViewModel
import com.kiwa.fluffit.presentation.battle.BattleViewState
import com.kiwa.fluffit.presentation.battle.ui.DefaultUI
import com.kiwa.fluffit.presentation.battle.ui.LoadingUI

@Composable
fun BattleScreen(
    viewModel: BattleViewModel = hiltViewModel<BattleViewModel>(),
    onStartGame: (GameUIModel) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(BattleViewEvent.Init)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val uiState: BattleViewState.Default =
            viewModel.uiState.collectAsState().value as BattleViewState.Default

        DefaultUI(
            Modifier.align(Alignment.Center), "배틀하기",
            uiState.battleStatistics
        ) {
            viewModel.onTriggerEvent(BattleViewEvent.OnClickBattleButton)
        }

        if (uiState.gameUIModel.battleId.isNotEmpty()) {
            onStartGame(uiState.gameUIModel)
        }

        when (uiState.loading) {
            true ->
                LoadingUI(
                    Modifier
                        .wrapContentSize()
                        .align(Alignment.Center)
                ) {
                    if (uiState.findMatching) Text(
                        text = "상대를 찾는 중입니다.\n(최대 30초 대기)",
                        style = MaterialTheme.typography.caption3
                    )
                }

            false -> {
            }
        }

        LaunchedEffect(uiState.message) {
            if (uiState.message.isNotEmpty()) Toast.makeText(
                context,
                uiState.message,
                Toast.LENGTH_SHORT
            ).show()
            viewModel.onTriggerEvent(BattleViewEvent.OnDismissToast)
        }


    }
}
