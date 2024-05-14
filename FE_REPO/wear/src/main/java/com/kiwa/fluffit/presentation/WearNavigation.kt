package com.kiwa.fluffit.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.kiwa.fluffit.model.battle.OpponentInfo
import com.kiwa.fluffit.presentation.game.GameScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val wearMainRoute = "wearMain"

internal fun NavGraphBuilder.wearMain(
    onNavigateToGame: (OpponentInfo) -> Unit,
) {
    composable(wearMainRoute) {
        WearApp(
            onNavigateToGame = onNavigateToGame
        )
    }
}

internal fun NavController.navigateToMain() {
    this.navigate(wearMainRoute)
}

internal fun NavGraphBuilder.game() {
    composable(
        "game/{opponentInfo}",
        arguments = listOf(navArgument("opponentInfo") { type = NavType.StringType })
    ) {
        val opponentJson = it.arguments?.getString("opponentInfo")
        val decodedOpponentJson = URLDecoder.decode(opponentJson, StandardCharsets.UTF_8.toString())
        val opponentInfo = Gson().fromJson(decodedOpponentJson, OpponentInfo::class.java)
        GameScreen(opponentInfo = opponentInfo)
    }
}

internal fun NavController.game(opponentInfo: OpponentInfo) {
    val opponentInfoJson = Gson().toJson(opponentInfo)
    val encodedOpponentInfoJson = URLEncoder.encode(opponentInfoJson, StandardCharsets.UTF_8.toString())
    this.navigate("game/$encodedOpponentInfoJson")
}
