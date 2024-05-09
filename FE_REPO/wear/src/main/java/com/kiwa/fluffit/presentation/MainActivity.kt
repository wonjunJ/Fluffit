package com.kiwa.fluffit.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.InputDeviceCompat
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewConfigurationCompat
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.HorizontalPageIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PageIndicatorState
import androidx.wear.compose.material.Text
import com.example.wearapp.presentation.HealthViewModel
import com.kiwa.fluffit.presentation.components.FeedButton
import com.kiwa.fluffit.presentation.screens.BattleScreen
import com.kiwa.fluffit.presentation.screens.ExerciseScreen
import com.kiwa.fluffit.presentation.screens.FeedScreen
import com.kiwa.fluffit.presentation.screens.MainScreen
import com.kiwa.fluffit.presentation.theme.FluffitTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val healthViewModel: HealthViewModel by viewModels()

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_SCROLL &&
                event.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)) {

                val delta = -event.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                        ViewConfigurationCompat.getScaledVerticalScrollFactor(
                            ViewConfiguration.get(this), this
                        )
                if (delta > 0) {
                    MainActivityViewModel.nextPage()
                } else if (delta < 0) {
                    MainActivityViewModel.previousPage()
                }
                Log.d(TAG, "onGenericMotionEvent: 회전 ${MainActivityViewModel.currentPage.value}")

                return true
            }
        }
        return super.onGenericMotionEvent(event)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 승인되지 않았다면 요청
            val REQUEST_CODE_ACTIVITY_RECOGNITION = 1
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_CODE_ACTIVITY_RECOGNITION)
        }

        setContent {
            FluffitTheme {
                WearApp()
            }
        }
    }

    companion object {
        const val PAGE_COUNT = 4;
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WearApp() {
    val currentPage by MainActivityViewModel.currentPage.collectAsState()
    val pagerState = rememberPagerState(pageCount = {MainActivity.PAGE_COUNT}, initialPage = currentPage)
    var showIndicator by remember { mutableStateOf(false) }


    val pageIndicatorState: PageIndicatorState = remember {
        object : PageIndicatorState {
            override val pageOffset: Float
                get() = 0f
            override val selectedPage: Int
                get() = pagerState.currentPage
            override val pageCount: Int
                get() = MainActivity.PAGE_COUNT
        }
    }

    LaunchedEffect(currentPage) {
        showIndicator = true
        pagerState.animateScrollToPage(currentPage, animationSpec = spring(dampingRatio = 0.8f, stiffness = 500f))
        delay(1000)
        showIndicator = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showIndicator,
            enter = slideInVertically { 5 },
            exit = slideOutVertically { 5 })
        {
            HorizontalPageIndicator(
                pageIndicatorState = pageIndicatorState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 3.dp)
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when(page){
                0 -> MainScreen()
                1 -> FeedScreen()
                2 -> ExerciseScreen()
                3 -> BattleScreen()
            }
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Box (modifier = Modifier.fillMaxSize()){
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            indicatorColor = Color.White,
            trackColor = Color.White.copy(alpha = 0.1f),
            strokeWidth = 4.dp
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            text = "$greetingName"
        )
        FeedButton()
    }
}
