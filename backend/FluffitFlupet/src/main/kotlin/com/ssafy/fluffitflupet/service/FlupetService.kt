package com.ssafy.fluffitflupet.service

import com.ssafy.fluffitflupet.client.MemberServiceClient
import com.ssafy.fluffitflupet.dto.FullResponse
import com.ssafy.fluffitflupet.dto.HealthResponse
import com.ssafy.fluffitflupet.dto.MainInfoResponse
import com.ssafy.fluffitflupet.entity.MemberFlupet
import com.ssafy.fluffitflupet.error.ErrorType
import com.ssafy.fluffitflupet.exception.CustomBadRequestException
import com.ssafy.fluffitflupet.repository.MemberFlupetRepository
import kotlinx.coroutines.*
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

    suspend fun updateNickname(userId: String, nickname: String?){
        val rgx = Regex("^[가-힣a-zA-Z0-9]+$") //한글 범위, 영어 대소문자 범위 및 숫자 범위를 포함하는 정규 표현식
        if(nickname.isNullOrEmpty()){
            throw CustomBadRequestException(ErrorType.TOO_SHORT_NICKNAME)
        }else if(nickname.length > 8){
            throw CustomBadRequestException(ErrorType.TOO_LONG_NICKNAME)
        }else if(!rgx.matches(nickname)){
            throw CustomBadRequestException(ErrorType.WRONG_CONDITION)
        }else {
            val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberId(userId) }
            if(mflupet == null){
                throw CustomBadRequestException(ErrorType.INVALID_USERID)
            }
            mflupet.name = nickname
            memberFlupetRepository.save(mflupet)
        }
    }

    suspend fun getFullness(userId: String): FullResponse {
        val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberId(userId) }
        if(mflupet == null){
            throw CustomBadRequestException(ErrorType.INVALID_USERID)
        }
        return FullResponse(
            fullness = mflupet.fullness,
            nextUpdateTime = (mflupet.fullnessUpdateTime!!.plusHours(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            isEvolutionAvailable = if(mflupet.exp == 100) true else false
        )
    }

    suspend fun getHealth(userId: String): HealthResponse {
        val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberId(userId) }
        if(mflupet == null){
            throw CustomBadRequestException(ErrorType.INVALID_USERID)
        }
        return HealthResponse(
            health = mflupet.health,
            nextUpdateTime = (mflupet.healthUpdateTime!!.plusHours(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            isEvolutionAvailable = if(mflupet.exp == 100) true else false
        )
    }
}