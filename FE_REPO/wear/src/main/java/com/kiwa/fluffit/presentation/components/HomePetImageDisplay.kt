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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.kiwa.fluffit.presentation.home.HomeViewModel
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

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(image)
            .apply {
                // Use ImageDecoderDecoder if API level is 28 or higher, else use GifDecoder
                decoderFactory(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
            }
            .build())


    LaunchedEffect(imageUrl) {
        if (imageUrl.isNotEmpty()) {
            image = if (imageUrl.size >= 2) {
                imageUrl[1]
            } else {
                imageUrl[0]
            }
        }
    }

    LaunchedEffect(showGif) {
        if (showGif) {
            if (imageUrl.size >= 4) {
                image = imageUrl[3]
            }

            delay(1000)
            showGif = false

            if (imageUrl.isNotEmpty()) {
                image = if (imageUrl.size >= 2) {
                    imageUrl[1]
                } else {
                    imageUrl[0]
                }
            }
        }
    }

    fun vibratePhone() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastVibrationTime >= vibrationInterval) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    30,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
            vibrationCount++
            lastVibrationTime = currentTime  // 마지막 진동 시간 갱신
        }
    }


    Image(
        modifier =
        Modifier
            .size(
                if (
                    image.contains("/rabbit_white_happy.gif") ||
                    image.contains("/cat_gray_happy.gif") ||
                    image.contains("/dog_corgi_happy.gif")
                ) {
                    200.dp
                } else {
                    100.dp
                }
            )
            .pointerInput(Unit) {
                awaitEachGesture {
                    do {
                        val event = awaitPointerEvent()
                        event.changes.forEach { change ->
                            change.consume()  // 이벤트 변경 사항 소비
                            vibratePhone()  // 드래그 동작 시 진동 발생


                            if (vibrationCount == 15) {
                                showGif = true
                                homeViewModel.patRequest {
                                    if (it) {

                                        Toast
                                            .makeText(context, "♥", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                                vibrationCount = 0
                            }
                        }
                    } while (event.changes.any { it.pressed })
                    vibrationCount = 0
                }
            },
        painter = painter,
        contentDescription = "home page pet image"
    )
}
