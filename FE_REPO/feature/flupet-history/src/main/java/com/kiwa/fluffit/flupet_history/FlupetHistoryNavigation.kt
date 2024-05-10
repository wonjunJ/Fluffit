package com.kiwa.fluffit.flupet_history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val flupetHistoryRoute = "flupet-history"

fun NavGraphBuilder.flupetHistoryScreen(){
    composable(flupetHistoryRoute){
        FlupetHistoryScreen()
    }
}

fun NavController.navigateToFlupetHistory(){
    this.navigate(flupetHistoryRoute)
}
