package com.ssafy.fluffitflupet.repository

import com.ssafy.fluffitflupet.entity.MemberFlupet
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MemberFlupetRepository: CoroutineCrudRepository<MemberFlupet, Long> {

}