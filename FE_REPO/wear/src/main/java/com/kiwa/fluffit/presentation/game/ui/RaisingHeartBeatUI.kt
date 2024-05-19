package com.kiwa.fluffit.presentation.game.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.kiwa.fluffit.presentation.health.HealthViewModel
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.util.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
internal fun RaisingHeartBeatUI(
    gameTime: Int,
    description: String,
    imageLoader: ImageLoader,
    viewModel: HealthViewModel = hiltViewModel<HealthViewModel>(),
    onFinishGame: (Int) -> Unit,
) {
    val descriptionVisibility = remember { mutableStateOf(false) }
    val descriptionCountDown = remember { mutableIntStateOf(3) }

    val time = gameTime * 1000L
    val timer = remember { mutableStateOf(time.formatTime()) }
    val startTime = remember { mutableLongStateOf(0L) }
    val isTimerRunning = remember { mutableStateOf(false) }

    val heartRate = viewModel.heartRate.collectAsState()

    val score = heartRate.value ?: 0

    val xOffset = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    val targetX = remember {
        mutableFloatStateOf(400f)
    }

    val runningImageIndex = remember {
        mutableIntStateOf(0)
    }

    val imageList =
        listOf(R.drawable.black_cat_run, R.drawable.corgi_run, R.drawable.white_rabbit_run)

    LaunchedEffect(isTimerRunning.value) {
        if (isTimerRunning.value && startTime.longValue == 0L) {
            startTime.longValue = System.currentTimeMillis()
            while (isTimerRunning.value) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - startTime.longValue
                val newTimer = (time - elapsedTime).formatTime()
                timer.value = newTimer
                delay(10)
                if (elapsedTime >= time) {
                    timer.value = 0L.formatTime()
                    isTimerRunning.value = false
                }
            }
        }
        if (!isTimerRunning.value && startTime.longValue > 0L) {
            delay(300)
            onFinishGame(score)
        }
    }

    LaunchedEffect(targetX.floatValue) {
        launch {
            xOffset.animateTo(
                targetValue = targetX.floatValue,
                animationSpec = TweenSpec(durationMillis = 5000, easing = LinearEasing),
            )
        }
        delay(5000)
        targetX.floatValue *= -1
        runningImageIndex.intValue = (runningImageIndex.intValue + 1) % 3
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = timer.value,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
            style = MaterialTheme.typography.display2
        )

        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.heartbeat, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.Center)
        )

        Box(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageList[runningImageIndex.intValue],
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .offset {
                        IntOffset(
                            xOffset.value.roundToInt(),
                            0
                        )
                    }
                    .scale(scaleX = if (targetX.floatValue > 0) 1f else -1f, scaleY = 1f)
            )
        }


        Text(
            text = "$score",
            style = MaterialTheme.typography.display1,
            modifier = Modifier.align(Alignment.BottomCenter)
        )


        if (startTime.longValue == 0L) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            )
            {
                Text(
                    text = description,
                    style = MaterialTheme.typography.display3
                )

                Text(
                    text = "${descriptionCountDown.intValue}",
                    style = MaterialTheme.typography.title1.merge(
                        colorResource(id = R.color.watchBlue)
                    )
                )
            }

            LaunchedEffect(Unit) {
                while (descriptionCountDown.intValue > 0) {
                    delay(1000)
                    descriptionCountDown.intValue -= 1

                    if (descriptionCountDown.intValue < 1) {
                        descriptionVisibility.value = false
                        isTimerRunning.value = true
                    }
                }
            }
        }
    }
}
