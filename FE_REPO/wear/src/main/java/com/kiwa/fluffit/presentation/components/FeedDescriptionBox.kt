package com.kiwa.fluffit.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.feed.FeedViewModel
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun FeedDescriptionBox() {
    val feedViewModel : FeedViewModel = hiltViewModel()
    val visible by feedViewModel.showDescription.collectAsState()


    AnimatedVisibility(
        modifier =
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (visible!!) {
                            feedViewModel.turnOffDescription()
                        }
                    }
                )
            },
        visible = visible!!,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)), // 점점 밝아지며 나타나는 효과
        exit = fadeOut(animationSpec = tween(durationMillis = 300))  // 점점 어두워지며 사라지는 효과
    ) {
        Box() {
            Box(modifier = Modifier
                .align(Alignment.Center)
                .width(120.dp)
                .height(120.dp)
                .background(color = Color.White, shape = RoundedCornerShape(30.dp))
                .padding(5.dp) ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "건강한 음식",
                        style = TextStyle(
                            fontFamily = fluffitWearFontFamily,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = "건강 +2",
                        style = TextStyle(
                            fontFamily = fluffitWearFontFamily,
                            color = colorResource(id = R.color.watchGreen),
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = "포만감 +2",
                        style = TextStyle(
                            fontFamily = fluffitWearFontFamily,
                            color = colorResource(id = R.color.watchBlue),
                            fontSize = 12.sp
                        )
                    )
                    Box(modifier =
                        Modifier.padding(top = 5.dp)
                    ) {
                        CoinDisplay(coin = 500, textColor = Color.Black)
                    }
                }
            }
        }
    }
}
