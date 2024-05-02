package com.ssafy.fluffitflupet.service

import com.ssafy.fluffitflupet.client.MemberServiceClient
import com.ssafy.fluffitflupet.dto.MainInfoResponse
import com.ssafy.fluffitflupet.entity.MemberFlupet
import com.ssafy.fluffitflupet.repository.MemberFlupetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Service
class FlupetService(
    private val memberFlupetRepository: MemberFlupetRepository,
    private val userServiceClient: MemberServiceClient
) {
    //lombok slf4j를 쓰기 위해
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    //우선 요청을 받으면 포만감과 건강의 업데이트 시간을 확인한 다음 최신 데이터를 업데이트를 하고 이후 그 flupet의 정보를 리턴
    suspend fun getMainInfo(userId: String): MainInfoResponse? {
//        coroutineScope {
//            var mainInfoDto = async { memberFlupetRepository.findMainInfoByUserId(userId) }
//            var coin = async { userServiceClient.getUserCoin(userId) }
//            var dto = mainInfoDto.await()
//            if(dto == null){ //현재 플러펫이 없다(mainInfoDto 연산이 완료될때까지 '블로킹되지 않고' 기다리게 된다)
//                var response = MainInfoResponse(
//                    fullness =
//                )
//            }
//            //return null
//        }
        return null
    }
}