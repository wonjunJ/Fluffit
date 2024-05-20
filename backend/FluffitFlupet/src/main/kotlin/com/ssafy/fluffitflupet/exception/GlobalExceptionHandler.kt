package com.ssafy.fluffitflupet.exception

import com.ssafy.fluffitflupet.error.ErrorResponse
import com.ssafy.fluffitflupet.error.ErrorResponse.Companion.from
import com.ssafy.fluffitflupet.error.ErrorResponse.Companion.of
import com.ssafy.fluffitflupet.service.FlupetService
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(
//        BadRequestException::class, NoHandlerFoundException::class, TypeMismatchException::class
//    )
//    fun handleBadRequest(e: Exception?): ErrorResponse {
//        //log.error("[BadRequestException]", e);
//        return of(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.")
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(
//        HttpRequestMethodNotSupportedException::class
//    )
//    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException?): ErrorResponse {
//        log.error("[HttpRequestMethodNotSupportedException]", e)
//        return of(HttpStatus.BAD_REQUEST, "지원 하지 않는 Http Method 입니다.")
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ErrorResponse {
        log.error("[ConstraintViolationException]", e)
        return of(HttpStatus.BAD_REQUEST, e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomBadRequestException::class)
    protected fun handleCustomBadRequestException(e: CustomBadRequestException): ErrorResponse {
        log.error("[CustomBadRequestException]")
        return from(e.errorType)
    }
}
