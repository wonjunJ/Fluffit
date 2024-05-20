package com.kiwa.data.model

sealed class FlupetStatusException : Exception() {

    data class FlupetIsDead(override val message: String) : FlupetStatusException()
    data class FlupetNotExist(override val message: String) : FlupetStatusException()
}
