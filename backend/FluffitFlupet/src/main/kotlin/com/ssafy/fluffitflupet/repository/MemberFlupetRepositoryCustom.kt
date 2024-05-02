package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.dto.MainInfoDto
import com.ssafy.fluffitflupet.dto.MainInfoResponse

interface MemberFlupetRepositoryCustom {
    suspend fun findMainInfoByUserId(userId: String): MainInfoDto?
}