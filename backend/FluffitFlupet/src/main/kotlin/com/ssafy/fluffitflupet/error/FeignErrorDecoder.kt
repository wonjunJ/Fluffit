package com.ssafy.fluffitflupet.error

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.stereotype.Component
import kotlin.Exception

//FeignClient 사용시 각 응답 상태코드에 대한 예외 처리
@Component
class FeignErrorDecoder: ErrorDecoder {
    override fun decode(methodKey: String?, response: Response?): Exception {
        when(response?.status()){
            in 400..499 -> {
                println(methodKey)
                return Exception(response?.reason())
            }
            else -> return Exception(response?.reason())
        }
    }
}