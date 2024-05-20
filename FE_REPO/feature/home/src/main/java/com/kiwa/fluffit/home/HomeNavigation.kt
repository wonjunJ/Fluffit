package com.kiwa.fluffit.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val homeRoute = "home"

fun NavGraphBuilder.homeScreen(
    onNavigateToCollection: () -> Unit,
    onNavigateToRankingDialog: () -> Unit,
    onNavigateToMyPage: () -> Unit
) {
    composable(homeRoute) {
        HomeRoute(
            onNavigateToRankingDialog = onNavigateToRankingDialog,
            onNavigateToCollection = onNavigateToCollection,
            onNavigateToMyPage = onNavigateToMyPage
        )
    }
}

fun NavController.navigateToHome() {
    this.navigate(homeRoute) {
        popUpTo("login") { inclusive = true }
    }
}
