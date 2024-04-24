package com.kiwa.fluffit

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiwa.fluffit.home.navigateToHome
import com.kiwa.fluffit.login.loginRoute
import com.kiwa.fluffit.login.loginScreen

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController()
){
    NavHost(navController = navController, startDestination = loginRoute){
        loginScreen {
            navController.navigateToHome()
        }
    }
}