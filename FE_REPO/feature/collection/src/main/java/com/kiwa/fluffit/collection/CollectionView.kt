package com.kiwa.fluffit.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.model.flupet.FlupetCollection

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
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(0.7f)
                .align(Alignment.Center)
                .background(Color.Red),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 0..collectionList.size - 1 step (2)) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = collectionList.get(i).species)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.padding(all = 10.dp)) {
                    for (j in i..i + 1) {
                        CollectionFlupetCard(collectionList, j)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
@Preview
fun checkCollectionView() {
    val list: MutableList<FlupetCollection> = mutableListOf()

    list.add(
        FlupetCollection(
            species = "하얀 토끼",
            imageUrl = "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
            tier = 1,
            metBefore = true
        )
    )
    list.add(
        FlupetCollection(
            species = "하얀 토끼",
            imageUrl = "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
            tier = 2,
            metBefore = false
        )
    )

    list.add(
        FlupetCollection(
            species = "갈색 토끼",
            imageUrl = "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
            tier = 1,
            metBefore = true
        )
    )
    list.add(
        FlupetCollection(
            species = "갈색 토끼",
            imageUrl = "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
            tier = 2,
            metBefore = true
        )
    )

    list.add(
        FlupetCollection(
            species = "검정 토끼",
            imageUrl = "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
            tier = 1,
            metBefore = true
        )
    )
    list.add(
        FlupetCollection(
            species = "검정 토끼",
            imageUrl = "https://github.com/shjung53/algorithm_study/assets/90888718/4399f85d-7810-464c-ad76-caae980ce047",
            tier = 2,
            metBefore = false
        )
    )

    CollectionView(collectionList = list)
}
