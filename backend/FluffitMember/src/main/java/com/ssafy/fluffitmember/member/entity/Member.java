package com.ssafy.fluffitmember.member.entity;


import com.ssafy.fluffitmember.exercise.entity.Running;
import com.ssafy.fluffitmember.exercise.entity.Steps;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private String memberId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int coin;

    @Column(nullable = false)
    private int battlePoint;

    @CreatedDate
    @Column(name = "join_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    private int mflupet_id;

    private LocalDateTime battlePointUpdateDate;

    @OneToMany(mappedBy = "member") // Running 엔티티 내 member 필드에 매핑됨
    private List<Running> runnings = new ArrayList<>();

    @OneToMany(mappedBy = "member") // Running 엔티티 내 member 필드에 매핑됨
    private List<Steps> steps = new ArrayList<>();

    @Builder
    public Member(String memberId,String socialId, String nickname, int coin, int battlePoint, int mflupetId, LocalDateTime battlePointUpdateDate) {
        this.memberId = memberId;
        this.socialId = socialId;
        this.nickname = nickname;
        this.coin = coin;
        this.battlePoint = battlePoint;
        this.mflupet_id = mflupetId;
        this.battlePointUpdateDate = battlePointUpdateDate;
    }
    public static Member of(String memberId,String socialId, String nickname, int coin, int battlePoint, int mflupetId, LocalDateTime battlePointUpdateDate) {
        return builder()
                .memberId(memberId)
                .socialId(socialId)
                .nickname(nickname)
                .coin(coin)
                .battlePoint(battlePoint)
                .mflupetId(mflupetId)
                .battlePointUpdateDate(battlePointUpdateDate)
                .build();
    }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateCoin(int coin) {
        this.coin = coin;
    }

    public void updatePoint(int point) {
        this.battlePoint = point;
    }

    public void updateBattleDate(LocalDateTime battleDate) {
        this.battlePointUpdateDate = battleDate;
    }
}
