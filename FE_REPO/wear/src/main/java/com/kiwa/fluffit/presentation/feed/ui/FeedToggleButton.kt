package com.kiwa.fluffit.presentation.feed.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ToggleButton
import com.kiwa.fluffit.presentation.feed.FeedViewEvent
import com.kiwa.fluffit.presentation.feed.FeedViewModel

@Composable
fun FeedToggleButton(
    feedImage: Painter,
    buttonId: Int,
    feedViewModel: FeedViewModel
) {
    val checkedButton by feedViewModel.selectedButtonIndex.collectAsState()
    val isChecked = checkedButton == buttonId

    ToggleButton(
        checked = isChecked,
        onCheckedChange = {
            feedViewModel.selectButton(buttonId)
            feedViewModel.onTriggerEvent(FeedViewEvent.SelectButton(buttonId))
        },
        modifier = Modifier.size(38.dp)
    ) {
        Image(modifier = Modifier.size(20.dp), painter = feedImage, contentDescription = "먹이")
    }
}

