package com.kiwa.fluffit.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.example.wearapp.presentation.HealthViewModel
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun HeartRateDisplay(modifier: Modifier) {
    val healthViewModel : HealthViewModel = hiltViewModel()
    val heartRate by healthViewModel.heartRate.collectAsState()

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Image(
            modifier = Modifier.size(20.dp).padding(end = 3.dp),
            painter = painterResource(R.drawable.heart_red),
            contentDescription = "heart"
        )
        Text(
            text = "$heartRate",
            fontFamily = fluffitWearFontFamily
        )
    }
}
