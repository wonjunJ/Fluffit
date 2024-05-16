package com.kiwa.fluffit.presentation.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.feed.FeedViewEvent
import com.kiwa.fluffit.presentation.feed.FeedViewModel
import com.kiwa.fluffit.presentation.feed.FeedViewState
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun FeedButton(
    feedViewState: FeedViewState,
    feedViewModel: FeedViewModel
) {
    Box(modifier = Modifier
        .padding(5.dp)
        .fillMaxSize()) {
        Button(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .width(80.dp)
                .height(30.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            onClick = { feedViewModel.onTriggerEvent(FeedViewEvent.FeedSelectedFood(feedViewState.feedNum + 1)) }
        ) {
            Text(text = stringResource(R.string.feed_button), fontFamily = fluffitWearFontFamily)
        }
    }
}
