package com.kiwa.fluffit.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.example.wearapp.presentation.HealthViewModel
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun CaloriesDisplay(modifier: Modifier, calories : Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(end = 1.dp)
    ) {
        Image(
            modifier = Modifier
                .size(20.dp),
            painter = painterResource(R.drawable.heart_red),
            contentDescription = "heart"
        )
        Text(
            fontSize = 15.sp,
            text = "$calories",
            color = Color.White
        )
    }
}
