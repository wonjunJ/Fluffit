package com.kiwa.fluffit.mypage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val myPageRoute = "mypage"

fun NavGraphBuilder.myPageScreen() {
    composable(myPageRoute) {
        MyPageScreen()
    }
}

fun NavController.navigateToMyPage() {
    this.navigate(myPageRoute)
}
