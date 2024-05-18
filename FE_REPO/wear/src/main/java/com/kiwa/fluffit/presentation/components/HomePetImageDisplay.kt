package com.kiwa.fluffit.presentation.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.kiwa.fluffit.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

private const val TAG = "HomePetImageDisplay"
@Composable
fun HomePetImageDisplay() {
//    val image = painterResource(R.drawable.dog_white)
    val context = LocalContext.current
    var lastVibrationTime = 0L
    val vibrationInterval = 150 // 진동 간격 500ms 설정
    var vibrationCount = 0

    val homeViewModel: HomeViewModel = hiltViewModel()
    val imageUrl by homeViewModel.imageUrl.collectAsState()

    var showGif by remember { mutableStateOf(false) }

    var image by remember { mutableStateOf("") }

    if(imageUrl.isNotEmpty()) {
        if(imageUrl.size >= 2) {
            image = imageUrl[1]
        } else {
            image = imageUrl[0]
        }
    }



    LaunchedEffect(showGif) {
        if (showGif) {
            Log.d(TAG, "쓰다듬기 사진 바꾸기")
            image = "https://my-fluffit-app-service-bucket.s3.ap-northeast-2.amazonaws.com/tombstone.png"
            delay(1000)
            showGif = false

            if (imageUrl.isNotEmpty()) {
                image = if (imageUrl.size >= 2) {
                    imageUrl[1]
                } else {
                    imageUrl[0]
                }
            }
            Log.d(TAG, "쓰다듬기 사진 복구")
        }
    }

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
            vibrationCount++
            lastVibrationTime = currentTime  // 마지막 진동 시간 갱신
        }
    }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
        .data(image)
        .apply {
            // Use ImageDecoderDecoder if API level is 28 or higher, else use GifDecoder
            decoderFactory(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
        }
        .build())

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


                            if (vibrationCount == 15) {
                                Log.d("TAG", "15 진동 감지")
                                showGif = true
                                homeViewModel.patRequest {
                                    if (it) {

                                        Toast
                                            .makeText(context, "♥", Toast.LENGTH_SHORT)
                                            .show()
                                        Log.d(TAG, "쓰다듬기 성공")
                                    } else {
                                        Log.d(TAG, "쓰다듬기 실패")
                                    }
                                }
                                vibrationCount = 0
                            }
                        }
                    } while (event.changes.any { it.pressed })
                    println("드래그 완료")
                    vibrationCount = 0
                }
            }
        ,
        painter = painter,
        contentDescription = "home page pet image"
    )
}

