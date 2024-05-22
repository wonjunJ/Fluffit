package com.ssafy.fluffitmember.exercise.entity;

import com.ssafy.fluffitmember.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Steps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(fetch = FetchType.LAZY) // 성능 최적화를 위해 LAZY 로딩 설정
    @JoinColumn(name = "member_pid") // 외래 키 이름 설정
    private Member member;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int stepCount;

    @Column(name = "member_id",nullable = false)
    private String memberId;

    @Builder
    public Steps(Member member, LocalDate date, int stepCount, String memberId){
        this.member = member;
        this.date = date;
        this.stepCount = stepCount;
        this.memberId = memberId;
    }

    public static Steps of(Member member, LocalDate date, int stepCount, String memberId){
        return builder()
                .member(member)
                .date(date)
                .stepCount(stepCount)
                .memberId(memberId)
                .build();
    }
    public void updateStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
