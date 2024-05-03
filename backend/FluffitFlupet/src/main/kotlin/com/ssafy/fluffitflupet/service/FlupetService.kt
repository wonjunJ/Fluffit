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
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Service
class FlupetService(
    private val memberFlupetRepository: MemberFlupetRepository,
    private val userServiceClient: MemberServiceClient
) {
    //lombok slf4j를 쓰기 위해
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    //우선 요청을 받으면 포만감과 건강의 업데이트 시간을 확인한 다음 최신 데이터를 업데이트를 하고 이후 그 flupet의 정보를 리턴
    suspend fun getMainInfo(userId: String): MainInfoResponse? = coroutineScope {
        println("여기왔나?")
        val mainInfoDto = async { memberFlupetRepository.findMainInfoByUserId(userId) }
        //ErrorDecoder로 오류 처리를 할지, 아니면 try~catch로 오류처리를 할지
        val coinWait = async(Dispatchers.IO) { userServiceClient.getUserCoin(userId) }
        val dto = mainInfoDto.await()
        val coin = coinWait.await()
        if(dto == null){ //현재 플러펫이 없다(mainInfoDto 연산이 완료될때까지 '블로킹되지 않고' 기다리게 된다)
            val response = MainInfoResponse()
            response.coin = coin
            return@coroutineScope response
        } else {
            val response = MainInfoResponse(
                fullness = dto.fullness,
                health = dto.health,
                flupetName = dto.flupetName,
                imageUrl = dto.imageUrl,
                birthDay = dto.birthDay.toLocalDate(),
                age = "${ChronoUnit.DAYS.between(dto.birthDay, LocalDate.now())}일 ${ChronoUnit.HOURS.between(dto.birthDay, LocalDate.now())}",
                isEvolutionAvailable = if(dto.exp == 100) true else false,
                nextFullnessUpdateTime = dto.nextFullnessUpdateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                nextHealthUpdateTime = dto.nextHealthUpdateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                coin = coin
            )
            return@coroutineScope response
        }
    }
}