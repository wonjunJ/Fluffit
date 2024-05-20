package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.dto.CollectionResponse
import com.ssafy.fluffitflupet.entity.Flupet
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FlupetRepository: CoroutineCrudRepository<Flupet, Long> {
    fun findByStage(stage: Int): Flow<Flupet>
}