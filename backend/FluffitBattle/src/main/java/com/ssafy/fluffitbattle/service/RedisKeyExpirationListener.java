package com.ssafy.fluffitbattle.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisKeyExpirationListener implements MessageListener {

    private final BattleService battleService;

    @Autowired
    public RedisKeyExpirationListener(BattleService battleService) {
        this.battleService = battleService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("expire! {}", expiredKey);
        if (expiredKey.startsWith("User:")) {
//            Long userId = Long.parseLong(expiredKey.split(":")[1]);
            battleService.handleTimeout(expiredKey.split(":")[1]);
        }
    }
}
