package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.entity.MemberFlupet
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberFlupetRepository: CoroutineCrudRepository<MemberFlupet, Long>, MemberFlupetRepositoryCustom {

}