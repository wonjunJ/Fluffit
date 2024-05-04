package com.ssafy.fluffitflupet.error

import org.springframework.http.HttpStatus

class ErrorResponse (
    val statusCode: Int, //http status code
    val msg: String //error message
) {
    companion object {
        fun from(errorType: ErrorType): ErrorResponse {
            return ErrorResponse(
                statusCode = errorType.status.value(),
                msg = errorType.msg
            )
        }

        fun of(status: HttpStatus, msg: String?): ErrorResponse {
            return ErrorResponse(
                statusCode = status.value(),
                msg = msg ?: status.reasonPhrase
            )
        }
    }
}
