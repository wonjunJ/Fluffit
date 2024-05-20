package com.kiwa.ranking

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog

const val ranking = "ranking"
fun NavGraphBuilder.ranking(onDismissDialog: () -> Unit) {
    dialog(ranking) {
        RankingRoute(onDismissDialog = onDismissDialog)
    }
}

fun NavController.navigateToRanking() {
    this.navigate(ranking)
}
