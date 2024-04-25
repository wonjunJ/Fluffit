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
    var flupetId: Long? = null,
    var memberId: String? = null,
    var name: String,
    var exp: Int = 0,
    var steps: Long = 0,
    var isDead: Boolean = false,
    @CreatedDate
    var createTime: LocalDateTime,
    var endTime: LocalDateTime,
    var fullness: Int = 100,
    var health: Int = 100,
    //쓰다듬기 남은 횟수
    var patCnt: Int = 5,
    //죽기 일보 직전인 시간
    var achaTime: LocalDateTime,
    var fullnessUpdateTime: LocalDateTime,
    var energyUpdateTime: LocalDateTime
) {
}