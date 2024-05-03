package com.kiwa.ranking.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.ranking.R
import com.kiwa.ranking.components.RankingFloor

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AgeRankingUI(ageRankingList: List<String>, onClickBattleRankingButton: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ranking_frame),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "장수 랭킹", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "배틀 랭킹 보기",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.End)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ranking_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    RankingFloor(rank = 2, Modifier.weight(0.33f))
                    RankingFloor(rank = 1, Modifier.weight(0.33f))
                    RankingFloor(rank = 3, Modifier.weight(0.33f))
                }
            }
            Row {
                val textStyle = MaterialTheme.typography.bodySmall
                Text(text = "102.", style = textStyle)
                Text(
                    text = "여덟글자닉네임임의 망해또입니다gdgd",
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    style = textStyle.merge(textAlign = TextAlign.Start),
                    maxLines = 1
                )
                Text(text = "500점", style = textStyle)
            }
        }
    }
}
