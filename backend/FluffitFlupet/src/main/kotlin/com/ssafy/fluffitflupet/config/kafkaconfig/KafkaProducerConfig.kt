package com.ssafy.fluffitflupet.config.kafkaconfig

//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.kafka.clients.producer.ProducerConfig
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.annotation.EnableKafka
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
//import org.springframework.kafka.core.*

//@EnableKafka
@Configuration
class KafkaProducerConfig {
    //접속할 수 있는 kafka의 정보
//    @Bean
//    fun producerFactory(): ProducerFactory<String, String> {
//        var properties = HashMap<String, Any>()
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "fluffit.shop:9092")
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
//        return DefaultKafkaProducerFactory(properties)
//    }
//
//    @Bean
//    fun kafkaTemplate(): KafkaTemplate<String, String> {
//        return KafkaTemplate(producerFactory())
//    }
}