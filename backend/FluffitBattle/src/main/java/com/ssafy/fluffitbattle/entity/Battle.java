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
//@RedisHash("Battle")
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long id;

    private String organizerId;
    private String participantId;
    private String winnerId;
    private LocalDateTime battleDate;

    @Enumerated(EnumType.STRING)
    private BattleType battleType;

    private Integer organizerScore;
    private Integer participantScore;

    @Builder
    public Battle(String organizerId, String participantId, BattleType battleType) {
        this.organizerId = organizerId;
        this.participantId = participantId;
        this.battleType = battleType;
        this.battleDate = LocalDateTime.now(); // 배틀 생성 시 현재 시간으로 설정
    }
}
