package com.ssafy.fluffitflupet.messagequeue

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.ssafy.fluffitflupet.dto.CoinKafkaDto
import com.ssafy.fluffitflupet.service.FlupetService
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    fun send(topic: String, coinKafkaDto: CoinKafkaDto) {
        val mapper = ObjectMapper()
        val jsonInString: String = try {
            mapper.writeValueAsString(coinKafkaDto) //객체를 json 포맷으로 변환
        }catch (ex: JsonProcessingException){
            ex.printStackTrace()
            return
        }
        kafkaTemplate.send(topic, jsonInString)
        log.info("Kafka Producer sent data from the Order microservice: $coinKafkaDto")
    }
}