package com.kiwa.fluffit.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.AnchorType
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.curvedComposable
import androidx.wear.compose.foundation.curvedRow
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.components.CoinDisplay
import com.kiwa.fluffit.presentation.components.FeedButton
import com.kiwa.fluffit.presentation.components.FeedDescriptionBox
import com.kiwa.fluffit.presentation.components.FeedDisplay
import com.kiwa.fluffit.presentation.components.FeedToggleButton

@Composable
fun FeedScreen() {
    val image = painterResource(R.drawable.feed_bag)
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)) {

        CurvedLayout(
            anchorType = AnchorType.Center,
//            angularDirection = CurvedDirection.Angular.,
            modifier = Modifier.fillMaxSize(),
            anchor = 270f,
        ) {
            curvedRow() {
                curvedComposable {
                    FeedToggleButton(feedImage = image, buttonId = 0)
                }
                curvedComposable {
                    FeedToggleButton(feedImage = image, buttonId = 1)
                }
                curvedComposable {
                    FeedToggleButton(feedImage = image, buttonId = 2)
                }
            }
        }
        Box(contentAlignment = Alignment.Center) {
            FeedDisplay()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.height(0.dp))
                Box(modifier = Modifier.padding(top = 5.dp)) {
                    CoinDisplay(coin = 13420)
                }
            }
        }
        FeedButton()
        FeedDescriptionBox()
    }
}
