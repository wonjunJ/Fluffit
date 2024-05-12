package com.kiwa.fluffit.presentation.battle

import androidx.compose.runtime.Composable
import com.kiwa.fluffit.presentation.components.BasicButton

@Composable
fun BattleButton(onClickBattleButton: () -> Unit) {
    BasicButton(onClickButton = onClickBattleButton, buttonText = "배틀하기")
}
