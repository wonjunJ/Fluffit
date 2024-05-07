package com.kiwa.fluffit.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.model.flupet.FlupetCollection

@Composable
internal fun CollectionFlupetCard(
    modifier : Modifier = Modifier,
    collectionList: List<FlupetCollection>,
    index: Int
) {
    Box(
        modifier = modifier.then(
            Modifier.aspectRatio(1f)
        )
    ) {
        Image(
            modifier = modifier.then(Modifier.fillMaxSize()),
            painter = painterResource(id = R.drawable.background),
            contentDescription = "도감 배경",
            contentScale = ContentScale.Crop
        )
    }
}
