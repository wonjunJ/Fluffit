package com.ssafy.fluffitmember.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.fluffitmember.member.entity.Member;
import com.ssafy.fluffitmember.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MemberRepository memberRepository;

    @KafkaListener(topics = "coin-update", groupId = "consumerGroupId")
    @Transactional
    public void updateCoin(String kafkaMessage) {
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        Optional<Member> findMember = memberRepository.findByMemberId((String) map.get("memberId"));

        if(findMember.isPresent()){
            Member member = findMember.get();
            member.updateCoin(member.getCoin() - (Integer)map.get("price"));
            memberRepository.save(member);
        }
    }

    @KafkaListener(topics = "battle-point-update", groupId = "consumerGroupId")
    @Transactional
    public void updateBattlePoint(String kafkaMessage) {
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        Optional<Member> findMember = memberRepository.findByMemberId((String) map.get("memberId"));

        if(findMember.isPresent()){
            Member member = findMember.get();
            int currentPoint = member.getBattlePoint();
            int battlePointChanges = (Integer)map.get("battlePointChanges");
            int updatePoint  = currentPoint + battlePointChanges;
            if(updatePoint < 0){
                member.updatePoint(0);
            }else{
                member.updatePoint(member.getBattlePoint() + (Integer)map.get("battlePointChanges"));
            }
            LocalDateTime now = LocalDateTime.now();
            member.updateBattleDate(now);
            memberRepository.save(member);
        }
    }
}
