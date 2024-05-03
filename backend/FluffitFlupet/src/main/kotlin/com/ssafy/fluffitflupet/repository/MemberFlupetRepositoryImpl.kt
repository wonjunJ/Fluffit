package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.dto.MainInfoDto
import com.ssafy.jooq.tables.Flupet
import com.ssafy.jooq.tables.MemberFlupet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext

class MemberFlupetRepositoryImpl(
    private val dslContext: DSLContext
): MemberFlupetRepositoryCustom {
    val MEMBER_FLUPET = MemberFlupet.MEMBER_FLUPET
    val FLUPET = Flupet.FLUPET

    override suspend fun findMainInfoByUserId(userId: String): MainInfoDto? {
        //return null
        return withContext(Dispatchers.IO) {
            dslContext
                .select( //fetch~into는 뒤에 as를 빼도되나??나중에 한번 테스트
                    MEMBER_FLUPET.FULLNESS.`as`("fullness"),
                    MEMBER_FLUPET.HEALTH.`as`("health"),
                    MEMBER_FLUPET.NAME.`as`("flupetName"),
                    MEMBER_FLUPET.EXP,
                    FLUPET.IMG_URL.`as`("imageUrl"),
                    MEMBER_FLUPET.CREATE_TIME.`as`("birthDay"),
                    MEMBER_FLUPET.FULLNESS_UPDATE_TIME.`as`("nextFullnessUpdateTime"),
                    MEMBER_FLUPET.HEALTH_UPDATE_TIME.`as`("nextHealthUpdateTime"),
                    MEMBER_FLUPET.IS_DEAD.`as`("isDead")
                )
                .from(MEMBER_FLUPET)
                .join(FLUPET)
                .on(MEMBER_FLUPET.FLUPET_ID.eq(FLUPET.ID))
                .where(MEMBER_FLUPET.MEMBER_ID.eq(userId))
                .orderBy(
                    MEMBER_FLUPET.END_TIME.isNull.desc(),
                    MEMBER_FLUPET.END_TIME.desc()
                )
                .limit(1)
                .fetchOneInto(MainInfoDto::class.java)
        }
    }

}