package com.ssafy.fluffitbattle.entity;

//import org.springframework.data.annotation.Id;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Battle")
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long id;

    private Long organizerId;
    private Long participantId;
    private Long winnerId;
    private LocalDateTime battleDate;

    @Enumerated(EnumType.STRING)
    private BattleType battleType;

    @Builder
    public Battle(Long organizerId, Long participantId, BattleType battleType) {
        this.organizerId = organizerId;
        this.participantId = participantId;
        this.battleType = battleType;
        this.battleDate = LocalDateTime.now(); // 배틀 생성 시 현재 시간으로 설정
    }
}
