package com.kiwa.fluffit.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val loginRoute = "login"

fun NavGraphBuilder.loginScreen(
    onNavigateToHome: () -> Unit
) {
    composable(loginRoute) {
        LoginScreen(
            onNavigationToHome = onNavigateToHome
        )
    }
}

fun NavController.navigateToLogin() {
    this.navigate(loginRoute)
}
