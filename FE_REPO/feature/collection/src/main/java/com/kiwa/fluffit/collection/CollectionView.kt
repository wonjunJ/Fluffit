package com.kiwa.fluffit.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.designsystem.theme.fluffitTypography
import com.kiwa.fluffit.model.FlupetCollection

@Composable
internal fun CollectionView(
    collectionList: List<FlupetCollection>
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.collection_background),
            contentDescription = "도감 배경화면",
            contentScale = ContentScale.FillBounds
        )

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(0.75f)
                .align(Alignment.Center)
                .padding(top = 30.dp)
        ) {
            for (i in 0..collectionList.size - 1 step (2)) {
                Text(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(start = 10.dp),
                    text = collectionList.get(i).species,
                    style = fluffitTypography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    CollectionFlupetCard(
                        modifier = Modifier.weight(1f),
                        collectionList,
                        i
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    CollectionFlupetCard(
                        modifier = Modifier.weight(1f),
                        collectionList,
                        i + 1
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
