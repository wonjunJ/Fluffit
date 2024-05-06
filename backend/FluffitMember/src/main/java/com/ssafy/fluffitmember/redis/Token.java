package com.ssafy.fluffitmember.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@RedisHash("refreshToken")
@AllArgsConstructor
@Builder
public class Token {
    @Id
    private String id;
    private String memberKey;
    private String refreshToken;
    private String accessToken;
    @TimeToLive
    private Long expiration;
}
