package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text

@Composable
fun CheckPhoneScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("휴대폰을 확인해 주세요.", modifier = Modifier.align(Alignment.Center))
    }
}
