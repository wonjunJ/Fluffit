package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.dto.*
import kotlinx.coroutines.flow.Flow

interface MemberFlupetRepositoryCustom {
    suspend fun findMainInfoByUserId(userId: String): MainInfoDto?
    fun findFlupetsByUserId(userId: String): Flow<CollectionResponse.Flupet>
    suspend fun findByMemberIdAndFlupet(userId: String): MyFlupetStateDto?
    fun findHistoryByUserId(userId: String): Flow<HistoryResponse.MyPet>
}