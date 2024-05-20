package com.kiwa.fluffit.presentation.components

import android.icu.text.DecimalFormat
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.presentation.health.HealthViewModel
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.home.HomeViewModel
import com.kiwa.fluffit.presentation.util.sendMessageToPhone
import kotlinx.coroutines.launch

@Composable
fun StepsDisplay() {
    val healthViewModel : HealthViewModel = hiltViewModel()
    val steps by healthViewModel.steps.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val homeViewModel: HomeViewModel = hiltViewModel()

    val context = LocalContext.current

    val formattedSteps = DecimalFormat("#,###").format(steps ?: 0)

    val onCoinUpdate: () -> Unit = { sendMessageToPhone(context) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                coroutineScope.launch {
                    val totalCoin = healthViewModel.sendCoinRequest()
                    totalCoin?.let {
                        Toast.makeText(context, "+${it.gainedCoin}코인", Toast.LENGTH_SHORT).show()
                        homeViewModel.setCoin(it.totalCoin)
                    }
                    onCoinUpdate()
                }
            }
            .padding(10.dp)
        ) {
        Image(
            modifier = Modifier.size(20.dp).padding(end = 3.dp),
            painter = painterResource(R.drawable.footprint),
            contentDescription = "footprint"
        )
        Text(
            fontSize = 15.sp,
            text = formattedSteps,
            color = Color.White
        )
    }

}
