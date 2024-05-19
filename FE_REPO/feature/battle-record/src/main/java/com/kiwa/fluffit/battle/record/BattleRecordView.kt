package com.kiwa.fluffit.battle.record

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwa.fluffit.battle_record.R
import com.kiwa.fluffit.designsystem.theme.fluffitTypography
import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics

private const val TAG = "BattleRecordView_싸피"

@Composable
internal fun BattleRecordView(
    stats: UserBattleStatistics,
    history: UserBattleHistory
) {
    val historyListState = rememberLazyListState()
    val game = stats.battleStatisticItemDtoList

    Log.d(TAG, "BattleRecordView: $stats")
    Log.d(TAG, "BattleRecordView: $history")

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.battlerecordbackground),
            contentDescription = "배경화면",
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(0.05f)
                    .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color(0xCCFFFFFF))
            ) {
                Text(
                    text = "현재 점수",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp),
                    style = fluffitTypography.headlineMedium
                )
                BattleRecordOutLinedText(
                    text = "${stats.battlePoint}",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp),
                    textStyle = fluffitTypography.headlineMedium.merge(
                        TextStyle(
                            fontSize = 28.sp,
                            color = Color(0xFF70F150)
                        )
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.25f)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color(0xCCFFFFFF))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = game[0].title,
                            style = fluffitTypography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Image(
                            painter = painterResource(R.drawable.stone),
                            contentDescription = "돌깨기",
                            modifier = Modifier
                                .fillMaxWidth(0.25f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillBounds
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row() {
                            Text(
                                text = game[0].totalCount.toString() + "전 ",
                                style = fluffitTypography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = game[0].winCount.toString(),
                                style = fluffitTypography.bodyMedium.merge(
                                    color = Color(0xFF70F150)
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "승 ",
                                style = fluffitTypography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = game[0].loseCount.toString(),
                                style = fluffitTypography.bodyMedium.merge(
                                    color = Color(0xFFEE5757)
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "패",
                                style = fluffitTypography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            text = "승률 " + game[0].winRate.toString() + "%",
                            style = fluffitTypography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Box(modifier = Modifier.weight(0.1f))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color(0xCCFFFFFF))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = game[1].title,
                            style = fluffitTypography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Image(
                            painter = painterResource(R.drawable.heart_red),
                            contentDescription = "심박수",
                            modifier = Modifier
                                .fillMaxWidth(0.25f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillBounds
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row() {
                            Text(
                                text = game[1].totalCount.toString() + "전 ",
                                style = fluffitTypography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = game[1].winCount.toString(),
                                style = fluffitTypography.bodyMedium.merge(
                                    color = Color(0xFF70F150)
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "승 ",
                                style = fluffitTypography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = game[1].loseCount.toString(),
                                style = fluffitTypography.bodyMedium.merge(
                                    color = Color(0xFFEE5757)
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "패",
                                style = fluffitTypography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            text = "승률 " + game[1].winRate.toString() + "%",
                            style = fluffitTypography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f),
                state = historyListState,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(history.content.size) { index ->
                    BattleHistoryLog(history.content[index])
                }
            }
        }
    }
}

@Composable
internal fun BattleRecordOutLinedText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    strokeColor: Color = Color.Black,
    strokeWidth: Float = 1.5f
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
    ) {
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            text = text,
            style = textStyle.merge(
                TextStyle(
                    color = strokeColor,
                    drawStyle = Stroke(width = strokeWidth, join = StrokeJoin.Round)
                )
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
