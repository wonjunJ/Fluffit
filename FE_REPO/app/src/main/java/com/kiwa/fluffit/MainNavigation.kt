package com.kiwa.fluffit

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.navigation.navOptions
import com.kiwa.fluffit.home.homeScreen
import com.kiwa.fluffit.home.navigateToHome
import com.kiwa.fluffit.login.loginRoute
import com.kiwa.fluffit.login.loginScreen

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

        homeScreen {
            navController.navigateUp()
        }
    }
}