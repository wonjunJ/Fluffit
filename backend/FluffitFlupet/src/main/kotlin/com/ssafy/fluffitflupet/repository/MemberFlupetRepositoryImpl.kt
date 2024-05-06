package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.dto.CollectionResponse
import com.ssafy.fluffitflupet.dto.MainInfoDto
import com.ssafy.jooq.tables.Flupet
import com.ssafy.jooq.tables.MemberFlupet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.jooq.impl.DSL

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

    override suspend fun findFlupetsByUserId(userId: String): Flow<CollectionResponse.Flupet> {
        return dslContext
            .select(
                FLUPET.NAME.`as`("species"),
                FLUPET.IMG_URL,
                FLUPET.STAGE.`as`("tier"),
                DSL.field( //jooq에서 임의의 새로운 필드를 생성하기 위한 방법
                    DSL.exists(
                        dslContext
                            .selectOne()
                            .from(MEMBER_FLUPET)
                            .where(
                                MEMBER_FLUPET.FLUPET_ID.eq(FLUPET.ID)
                                    .and(MEMBER_FLUPET.MEMBER_ID.eq(userId))
                            )
                    )
                ).`as`("metBefore")
            )
            .from(FLUPET)
            .orderBy(
                FLUPET.ID.asc()
            )
            .fetchInto(CollectionResponse.Flupet::class.java)
            .asFlow()
    }

}