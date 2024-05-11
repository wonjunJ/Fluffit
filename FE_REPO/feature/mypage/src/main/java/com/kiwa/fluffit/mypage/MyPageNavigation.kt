package com.kiwa.fluffit.mypage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val myPageRoute = "mypage"

fun NavGraphBuilder.myPageScreen(
    onNavigateToFlupetHistory: () -> Unit,
    onNavigateToBattleRecord: () -> Unit,
    onNavigateToLogout: () -> Unit
) {
    composable(myPageRoute) {
        MyPageScreen(
            onClickFlupetHistory = onNavigateToFlupetHistory,
            onClickBattleRecord = onNavigateToBattleRecord,
            onClickLogout = onNavigateToLogout
        )
    }
}

fun NavController.navigateToMyPage() {
    this.navigate(myPageRoute)
}
