package com.kiwa.fluffit.battle.record

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kiwa.fluffit.battle_record.R
import com.kiwa.fluffit.model.UserBattleHistory
import com.kiwa.fluffit.model.UserBattleStatistics

private const val TAG = "BattleRecordView_싸피"
@Composable
internal fun BattleRecordView(
    stats: UserBattleStatistics,
    history: UserBattleHistory
) {
    val statsListState = rememberLazyListState()
    val historyListState = rememberLazyListState()

    Log.d(TAG, "BattleRecordView: ${stats}")
    Log.d(TAG, "BattleRecordView: ${history}")

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.battlerecordbackground),
            contentDescription = "배경화면",
            contentScale = ContentScale.FillBounds
        )


    }
}
