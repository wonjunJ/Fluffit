package com.ssafy.fluffitbattle.service;

import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ssafy.fluffitbattle.entity.BattleType;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final NotificationService notificationService;
    private final BattleRepository battleRepository;
    private Random random = new Random();

    private static final String MATCH_QUEUE = "matchQueue";

    public void addToQueue(Long userId) {
        ListOperations<String, Long> listOps = redisTemplate.opsForList();
        listOps.rightPush(MATCH_QUEUE, userId);
        tryMatch();
    }

    private void tryMatch() {
        ListOperations<String, Long> listOps = redisTemplate.opsForList();
        while (listOps.size(MATCH_QUEUE) >= 2) {
            Long organizerId = listOps.leftPop(MATCH_QUEUE);
            Long participantId = listOps.leftPop(MATCH_QUEUE);

            if (organizerId != null && participantId != null) {
                Battle battle = Battle.builder()
                        .organizerId(organizerId)
                        .participantId(participantId)
                        .battleType(BattleType.values()[random.nextInt(BattleType.values().length)])
                        .build();
                battleRepository.save(battle);
                notificationService.notifyMatching(battle);
            }
        }
    }
}
