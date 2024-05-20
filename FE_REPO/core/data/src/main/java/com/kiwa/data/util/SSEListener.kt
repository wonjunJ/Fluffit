package com.kiwa.data.util

interface SSEListener {
    fun <T> getResult(): Result<T>
}
