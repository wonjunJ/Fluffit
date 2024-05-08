//package com.ssafy.fluffitbattle.service;
//
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//@Component
//public class BattleMatchMaker {
//    private final ReactiveRedisTemplate<String, String> redisTemplate;
//    private final ChannelTopic topic;
//
//    public BattleMatchMaker(ReactiveRedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.topic = new ChannelTopic("battleNotifications");
//    }
//
//    public void publishBattleStart(String battleId) {
//        redisTemplate.convertAndSend(topic.getTopic(), "Battle " + battleId + " is starting now!").subscribe();
//    }
//
//    public Mono<String> findMatch(String userId) {
//        return redisTemplate.opsForList().rightPop("battleQueue")
//                .switchIfEmpty(Mono.defer(() -> {
//                    redisTemplate.opsForList().leftPush("battleQueue", userId).subscribe();
//                    return Mono.empty();
//                }));
//    }
//}
