package com.kiwa.fluffit.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val homeRoute = "home"

fun NavGraphBuilder.homeScreen() {
    composable(homeRoute) {
        HomeScreen()
    }
}

fun NavController.navigateToHome(){
//    this.navigate()
}