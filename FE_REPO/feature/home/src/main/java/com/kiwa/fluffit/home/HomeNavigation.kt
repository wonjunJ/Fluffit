package com.kiwa.fluffit.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val homeRoute = "home"
fun NavGraphBuilder.homeScreen(onNavigateToRankingDialog: () -> Unit) {
    composable(homeRoute) {
        HomeRoute(onNavigateToRankingDialog = { onNavigateToRankingDialog() })
    }
}

fun NavController.navigateToHome() {
    this.navigate(homeRoute) {
        popUpTo("login") { inclusive = true }
    }
}
