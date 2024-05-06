package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.dto.CollectionResponse
import com.ssafy.fluffitflupet.dto.MainInfoDto
import com.ssafy.fluffitflupet.dto.MainInfoResponse
import kotlinx.coroutines.flow.Flow

interface MemberFlupetRepositoryCustom {
    suspend fun findMainInfoByUserId(userId: String): MainInfoDto?
    suspend fun findFlupetsByUserId(userId: String): Flow<CollectionResponse.Flupet>
}