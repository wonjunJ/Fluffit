package com.kiwa.fluffit.flupet.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.flupet_history.R
import com.kiwa.fluffit.model.flupet.response.MyFlupets

@Composable
internal fun FlupetHistoryView(
    flupetHistoryList: List<MyFlupets>
) {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.flupet_history_background),
            contentDescription = "배경화면",
            contentScale = ContentScale.FillBounds
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.8f)
                .align(Alignment.Center),
            state = listState,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(flupetHistoryList.size) { index ->
                FlupetLog(flupetHistoryList[index])
            }
        }
    }
}
