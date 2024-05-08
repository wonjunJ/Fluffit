package com.ssafy.fluffitflupet.messagequeue

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
//import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer {
//    @KafkaListener(topics = ["ex_temp"])
//    fun update(message: String) {
//        //var map = HashMap<Any, Any>()
//        val mapper = ObjectMapper()
//        val map: Map<Any, Any> = try {
//            mapper.readValue(message, object : TypeReference<Map<Any, Any>>() {})
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//            return
//        }
//    }
}