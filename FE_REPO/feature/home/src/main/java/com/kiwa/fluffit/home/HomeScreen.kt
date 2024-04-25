package com.kiwa.fluffit.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HomeScreen(
    viewModel : HomeViewModel = hiltViewModel<HomeViewModel>()
){
    Text(text = " hi HomeScreen")
}