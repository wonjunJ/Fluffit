package com.kiwa.ranking.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import com.kiwa.fluffit.model.ranking.RankingInfo
import com.kiwa.fluffit.ranking.R
import com.kiwa.ranking.components.RankingFloor

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RankingUI(
    rankingList: List<RankingInfo> = emptyList(),
    myRanking: RankingInfo,
    onClickShowAnotherRankingButton: () -> Unit,
    rankingType: String,
    anotherRankingType: String
) {
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
            Text(text = rankingType, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = anotherRankingType,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.End)
                    .clickable { onClickShowAnotherRankingButton() }
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
                    RankingFloor(
                        rankingInfo = rankingList.find { rankInfo -> rankInfo.rank == 2 }
                            ?: RankingInfo(2, "", "", "", ""),
                        Modifier.weight(0.33f)
                    )
                    RankingFloor(
                        rankingInfo = rankingList.find { rankInfo -> rankInfo.rank == 1 }
                            ?: RankingInfo(
                                1,
                                "",
                                "",
                                "",
                                "https://github.com/shjung53/algorithm_study/assets/" +
                                    "90888718/4399f85d-7810-464c-ad76-caae980ce047"
                            ),
                        Modifier.weight(0.33f)
                    )
                    RankingFloor(
                        rankingInfo = rankingList.find { rankInfo -> rankInfo.rank == 3 }
                            ?: RankingInfo(3, "", "", "", ""),
                        Modifier.weight(0.33f)
                    )
                }
            }
            Row {
                val textStyle = MaterialTheme.typography.bodySmall
                Text(text = "${myRanking.rank}.", style = textStyle)
                Text(
                    text = myRanking.userName,
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    style = textStyle.merge(textAlign = TextAlign.Start),
                    maxLines = 1
                )
                Text(text = myRanking.score, style = textStyle)
            }
        }
    }
}
