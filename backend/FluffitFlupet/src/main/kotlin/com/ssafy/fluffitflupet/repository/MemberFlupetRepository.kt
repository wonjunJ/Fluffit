package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.entity.MemberFlupet
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MemberFlupetRepository: ReactiveCrudRepository<MemberFlupet, Long>, MemberFlupetRepositoryCustom {
    fun findAllByIsDeadIsFalse(): Flux<MemberFlupet>
}