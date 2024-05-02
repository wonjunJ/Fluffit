package com.kiwa.fluffit.collection

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val collectionRoute = "collection"

fun NavGraphBuilder.CollectionScreen(){
    composable(collectionRoute){
        CollectionScreen()
    }
}
