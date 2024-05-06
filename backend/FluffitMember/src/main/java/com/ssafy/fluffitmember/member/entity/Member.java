package com.ssafy.fluffitmember.member.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Member(String memberId,String socialId, String nickname, int coin, int battlePoint, int mflupetId) {
        this.memberId = memberId;
        this.socialId = socialId;
        this.nickname = nickname;
        this.coin = coin;
        this.battlePoint = battlePoint;
        this.mflupet_id = mflupetId;
    }
    public static Member of(String memberId,String socialId, String nickname, int coin, int battlePoint, int mflupetId) {
        return builder()
                .memberId(memberId)
                .socialId(socialId)
                .nickname(nickname)
                .coin(coin)
                .battlePoint(battlePoint)
                .mflupetId(mflupetId)
                .build();
    }

}
