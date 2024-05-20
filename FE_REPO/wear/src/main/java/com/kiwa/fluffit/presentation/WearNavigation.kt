package com.kiwa.fluffit.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.kiwa.fluffit.model.battle.GameUIModel
import com.kiwa.fluffit.presentation.game.GameScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val wearMainRoute = "wearMain"

internal fun NavGraphBuilder.wearMain(
    onNavigateToGame: (GameUIModel) -> Unit,
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

internal fun NavGraphBuilder.game(onNavigateToMain: () -> Unit) {
    composable(
        "game/{gameUIModel}",
        arguments = listOf(navArgument("gameUIModel") { type = NavType.StringType })
    ) {
        val opponentJson = it.arguments?.getString("gameUIModel")
        val decodedOpponentJson = URLDecoder.decode(opponentJson, StandardCharsets.UTF_8.toString())
        val gameUIModel = Gson().fromJson(decodedOpponentJson, GameUIModel::class.java)
        GameScreen(gameUIModel = gameUIModel, onFinishBattle = onNavigateToMain)
    }
}

internal fun NavController.game(gameUIModel: GameUIModel) {
    val gameUIModelJson = Gson().toJson(gameUIModel)
    val encodedGameUIModelJson =
        URLEncoder.encode(gameUIModelJson, StandardCharsets.UTF_8.toString())
    this.navigate("game/$encodedGameUIModelJson")
}
