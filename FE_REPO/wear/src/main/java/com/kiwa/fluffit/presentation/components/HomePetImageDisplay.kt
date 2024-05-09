package com.kiwa.fluffit.presentation.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun HomePetImageDisplay(image: Painter) {
//    val image = painterResource(R.drawable.dog_white)
    val context = LocalContext.current
    var lastVibrationTime = 0L
    val vibrationInterval = 150 // 진동 간격 500ms 설정

    fun vibratePhone() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastVibrationTime >= vibrationInterval) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(50)
            }
            lastVibrationTime = currentTime  // 마지막 진동 시간 갱신
        }
    }

    Image(
        modifier =
        Modifier
            .size(100.dp)
            .pointerInput(Unit) {
                awaitEachGesture {
                    do {
                        val event = awaitPointerEvent()
                        event.changes.forEach { change ->
                            change.consume()  // 이벤트 변경 사항 소비
                            vibratePhone()  // 드래그 동작 시 진동 발생
                            println("드래그 중: ${change.position}")
                        }
                    } while (event.changes.any { it.pressed })
                    println("드래그 완료")
                }
            }
        ,
        painter = image,
        contentDescription = "home page pet image"
    )
}

