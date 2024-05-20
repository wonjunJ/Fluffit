package com.kiwa.fluffit.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiwa.fluffit.home.R

@Composable
internal fun TombStoneUI(modifier: Modifier, name: String) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.tombstone),
            contentDescription = null,
            modifier = modifier.size(200.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.wrapContentWidth(),
            maxLines = 1
        )
    }
}
