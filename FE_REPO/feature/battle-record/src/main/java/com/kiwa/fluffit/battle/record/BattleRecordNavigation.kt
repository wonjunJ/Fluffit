package com.kiwa.fluffit.battle.record

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val battleRecordRoute = "battleRecord"

fun NavGraphBuilder.battleRecordScreen() {
    composable(battleRecordRoute) {
        BattleRecordScreen()
    }
}

fun NavController.navigateToBattleRecord() {
    this.navigate(battleRecordRoute)
}
