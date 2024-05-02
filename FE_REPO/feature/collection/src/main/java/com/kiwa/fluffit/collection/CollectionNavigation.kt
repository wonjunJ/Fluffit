package com.kiwa.fluffit.collection

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val collectionRoute = "collection"

fun NavGraphBuilder.collectionScreen(){
    composable(collectionRoute){
        CollectionScreen()
    }
}

fun NavController.navigateToCollection(){
    this.navigate(collectionRoute)
}
