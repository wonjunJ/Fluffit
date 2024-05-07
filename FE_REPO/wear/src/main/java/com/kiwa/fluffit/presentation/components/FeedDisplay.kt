package com.kiwa.fluffit.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.Feed.FeedViewModel
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun FeedDisplay() {
    val feedViewModel : FeedViewModel = hiltViewModel()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
        ) {
        Button( // 물음표 버튼
            modifier = Modifier.size(38.dp),
            onClick = {
                      feedViewModel.turnOnDescription()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,)
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.question_mark),
                contentDescription = "물음표"
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "건강식",
                fontFamily = fluffitWearFontFamily
            )
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.feed_bag),
                contentDescription = "큰 사료 이미지"
            )
        }
        Spacer(modifier = Modifier.size(38.dp))
    }

}