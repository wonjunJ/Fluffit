package com.ssafy.fluffitbattle.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
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

//    @Builder
//    public Battle(
//            Long organizerId, Long participantId,
//    )
}
