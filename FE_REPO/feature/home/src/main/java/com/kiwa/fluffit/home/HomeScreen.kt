package com.kiwa.fluffit.home

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.size.OriginalSize
import com.kiwa.fluffit.home.ui.components.CoinDisplay
import com.kiwa.fluffit.home.ui.components.FlupetImageButton
import com.kiwa.fluffit.home.ui.components.StatDisplay

@Composable
internal fun HomeRoute(viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()) {
    val uiState: HomeViewState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(uiState = uiState)
}

@Composable
internal fun HomeScreen(
    uiState: HomeViewState
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        MainButtons()

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 80.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            FullnessDisplay(stat = 100)
            HealthDisplay(stat = 100)
            Image(
                painter = rememberImagePainter(
                    imageLoader = imageLoader,
                    data = "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
                    builder = {
                        size(OriginalSize)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 32.dp)
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "도끼보다토끼", style = MaterialTheme.typography.bodyMedium)
                Image(
                    painter = painterResource(id = R.drawable.pencil),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun FullnessDisplay(stat: Int) {
    val image = when (stat) {
        0 -> R.drawable.fullness_0
        in 1..20 -> R.drawable.fullness_20
        in 21..40 -> R.drawable.fullness_40
        in 41..60 -> R.drawable.fullness_60
        in 61..80 -> R.drawable.fullness_80
        else -> R.drawable.fullness_100
    }
    StatDisplay(statIconImage = R.drawable.fullness, statProgressImage = image)
}

@Composable
private fun HealthDisplay(stat: Int) {
    val image = when (stat) {
        0 -> R.drawable.health_0
        in 1..20 -> R.drawable.health_20
        in 21..40 -> R.drawable.health_40
        in 41..60 -> R.drawable.health_60
        in 61..80 -> R.drawable.health_80
        else -> R.drawable.health_100
    }
    StatDisplay(statIconImage = R.drawable.health, statProgressImage = image)
}

@Composable
private fun MainButtons() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 120.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.TopCenter), verticalAlignment = Alignment.Bottom) {
            CoinDisplay(coin = 1000)
            Spacer(modifier = Modifier.weight(1f))
            RankingButton()
        }
        CollectionButton(modifier = Modifier.align(Alignment.BottomStart))
        UserButton(modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
private fun CollectionButton(modifier: Modifier) {
    FlupetImageButton(id = R.drawable.collection, modifier = modifier)
}

@Composable
private fun UserButton(modifier: Modifier) {
    FlupetImageButton(id = R.drawable.user, modifier = modifier)
}

@Composable
private fun RankingButton() {
    FlupetImageButton(id = R.drawable.ranking)
}
