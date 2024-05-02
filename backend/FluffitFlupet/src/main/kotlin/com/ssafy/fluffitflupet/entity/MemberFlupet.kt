package com.ssafy.fluffitflupet.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("member_flupet")
data class MemberFlupet(
    @Id
    val id: Long? = null,
    @Column("flupet_id")
    var flupetId: Long? = null,
    @Column("member_id")
    var memberId: String? = null,
    var name: String,
    var exp: Int = 0,
    var steps: Long = 0,
    @Column("is_dead")
    var isDead: Boolean = false,
    //태어난 날짜
    @CreatedDate
    @Column("create_time")
    var createTime: LocalDateTime? = null,
    //죽은 날짜
    @Column("end_time")
    var endTime: LocalDateTime? = null,
    var fullness: Int = 100,
    var health: Int = 100,
    //쓰다듬기 남은 횟수
    @Column("pat_cnt")
    var patCnt: Int = 5,
    //죽기 일보 직전인 시간(예상 죽을 시간 1시간전) - user에게 push알림을 보내기 위해
    @Column("acha_time")
    var achaTime: LocalDateTime,
    @Column("fullness_update_time")
    var fullnessUpdateTime: LocalDateTime? = null,
    @Column("energy_update_time")
    var energyUpdateTime: LocalDateTime? = null
) {
}