package com.kiwa.fluffit.model.flupet.response

data class FlupetCollectionResponse(
    val species : String,
    val imgURL : String,
    val tier : Int,
    val metBefore : Boolean
)
