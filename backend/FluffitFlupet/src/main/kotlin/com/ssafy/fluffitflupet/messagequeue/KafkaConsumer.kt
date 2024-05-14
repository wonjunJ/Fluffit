package com.ssafy.fluffitflupet.messagequeue

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.ssafy.fluffitflupet.service.FlupetService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer {
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    @KafkaListener(topics = ["steps-update"], groupId = "consumerGroupId")
    fun update(message: String) {
        log.info("카프카로부터 제대로 메세지를 받나?? [$message]")
        //var map = HashMap<Any, Any>()
        val mapper = ObjectMapper()
        val map: Map<Any, Any> = try {
            mapper.readValue(message, object : TypeReference<Map<Any, Any>>() {})
        } catch (ex: Exception) {
            ex.printStackTrace()
            return
        }
    }
}