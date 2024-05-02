package com.kiwa.fluffit.collection

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel<CollectionViewModel>(),
    onNavigationToHome: () -> Unit
){

}

