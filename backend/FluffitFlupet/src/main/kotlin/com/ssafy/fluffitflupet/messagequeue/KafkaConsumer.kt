package com.ssafy.fluffitflupet.messagequeue

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.ssafy.fluffitflupet.repository.MemberFlupetRepository
import com.ssafy.fluffitflupet.service.FlupetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
    private val memberFlupetRepository: MemberFlupetRepository
) {
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    @KafkaListener(topics = ["steps-update"], groupId = "consumerGroupId")
    suspend fun update(message: String) {
        log.info("카프카로부터 제대로 메세지를 받나?? [$message]")
        //var map2 = HashMap<Any, Any>()
        val mapper = ObjectMapper()
        val map: Map<Any, Any> = try {
            mapper.readValue(message, object : TypeReference<Map<Any, Any>>() {}) //String형을 json타입으로 변환
        } catch (ex: Exception) {
            ex.printStackTrace()
            return
        }

        val userId = map["memberId"] as String
        val steps = map["steps"] as Long
        val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
        if(mflupet != null){
            mflupet.steps = steps
            withContext(Dispatchers.IO){ memberFlupetRepository.save(mflupet).awaitSingleOrNull() }
        }
    }
}