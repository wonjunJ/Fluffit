package com.kiwa.fluffit

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiwa.fluffit.battle.record.battleRecordScreen
import com.kiwa.fluffit.battle.record.navigateToBattleRecord
import com.kiwa.fluffit.collection.collectionScreen
import com.kiwa.fluffit.collection.navigateToCollection
import com.kiwa.fluffit.flupet.history.flupetHistoryScreen
import com.kiwa.fluffit.flupet.history.navigateToFlupetHistory
import com.kiwa.fluffit.home.homeScreen
import com.kiwa.fluffit.home.navigateToHome
import com.kiwa.fluffit.login.loginRoute
import com.kiwa.fluffit.login.loginScreen
import com.kiwa.fluffit.login.navigateToLogin
import com.kiwa.fluffit.mypage.myPageScreen
import com.kiwa.fluffit.mypage.navigateToMyPage
import com.kiwa.ranking.navigateToRanking
import com.kiwa.ranking.ranking

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = loginRoute,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.systemBars.only(WindowInsetsSides.Vertical)
            )
    ) {
        loginScreen {
            navController.navigateToHome()
        }

        homeScreen(
            onNavigateToCollection = { navController.navigateToCollection() },
            onNavigateToRankingDialog = { navController.navigateToRanking() },
            onNavigateToMyPage = { navController.navigateToMyPage() }
        )

        ranking { navController.popBackStack() }

        collectionScreen()

        myPageScreen(
            onNavigateToFlupetHistory = { navController.navigateToFlupetHistory() },
            onNavigateToLogout = { navController.navigateToLogin() },
            onNavigateToBattleRecord = { navController.navigateToBattleRecord() }
        )

        flupetHistoryScreen()
        battleRecordScreen()
    }
}
