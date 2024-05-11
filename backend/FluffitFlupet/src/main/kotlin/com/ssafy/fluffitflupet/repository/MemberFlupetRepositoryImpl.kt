package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.dto.CollectionResponse
import com.ssafy.fluffitflupet.dto.MainInfoDto
import com.ssafy.fluffitflupet.dto.MyFlupetStateDto
import com.ssafy.jooq.tables.Flupet
import com.ssafy.jooq.tables.MemberFlupet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.jooq.impl.DSL
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MemberFlupetRepositoryImpl(
    private val dslContext: DSLContext
): MemberFlupetRepositoryCustom {
    val MEMBER_FLUPET = MemberFlupet.MEMBER_FLUPET
    val FLUPET = Flupet.FLUPET

    override suspend fun findMainInfoByUserId(userId: String): MainInfoDto? {
        //return null
        return withContext(Dispatchers.IO) {
            Mono.from(
                dslContext
                    .select( //fetch~into는 뒤에 as를 빼도되나??나중에 한번 테스트
                        MEMBER_FLUPET.FULLNESS.`as`("fullness"),
                        MEMBER_FLUPET.HEALTH.`as`("health"),
                        MEMBER_FLUPET.NAME.`as`("flupetName"),
                        MEMBER_FLUPET.EXP.`as`("exp"),
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
            )
                .map { record -> record.into(MainInfoDto::class.java) }
                .awaitFirstOrNull()
        }
    }

    override fun findFlupetsByUserId(userId: String): Flow<CollectionResponse.Flupet> {
        return Flux.from(
            dslContext
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
        )
            .map { record ->
                CollectionResponse.Flupet(
                    species = record.get("species", String::class.java),
                    imageUrl = record.get(FLUPET.IMG_URL, String::class.java).split(","),
                    tier = record.get("tier", Int::class.java),
                    metBefore = record.get("metBefore", Boolean::class.java)
                )
            }
            .asFlow()
        //return dslContext.selectOne().fetchInto(CollectionResponse.Flupet::class.java).asFlow()
    }

    override suspend fun findByMemberIdAndFlupet(userId: String): MyFlupetStateDto? {
        return Mono.from(
            dslContext
                .select(
                    MEMBER_FLUPET.ID.`as`("id"),
                    FLUPET.ID.`as`("flupetId"),
                    FLUPET.STAGE.`as`("stage")
                )
                .from(MEMBER_FLUPET)
                .join(FLUPET)
                .on(MEMBER_FLUPET.FLUPET_ID.eq(FLUPET.ID))
                .where(MEMBER_FLUPET.MEMBER_ID.eq(userId)
                    .and(MEMBER_FLUPET.IS_DEAD.isTrue))
                .limit(1)
        )
            .map { record -> record.into(MyFlupetStateDto::class.java) }
            .awaitFirstOrNull()
    }

}