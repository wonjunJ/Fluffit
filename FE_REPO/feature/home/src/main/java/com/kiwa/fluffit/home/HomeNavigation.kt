package com.kiwa.fluffit.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeRoute = "home"
fun NavGraphBuilder.homeScreen(onBackPressed: () -> Boolean) {
    composable(homeRoute) {
        HomeRoute()
    }
}

fun NavController.navigateToHome() {
    this.navigate(homeRoute) {
        popUpTo("login") { inclusive = true }
    }
}