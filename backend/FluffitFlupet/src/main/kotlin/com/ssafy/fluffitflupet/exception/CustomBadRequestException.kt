package com.ssafy.fluffitflupet.exception

import com.ssafy.fluffitflupet.error.ErrorType


class CustomBadRequestException(
    val errorType: ErrorType
) : RuntimeException() {
}
