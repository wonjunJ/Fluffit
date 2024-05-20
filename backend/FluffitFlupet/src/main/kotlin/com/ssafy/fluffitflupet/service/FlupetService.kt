package com.ssafy.fluffitflupet.service

import com.ssafy.fluffitflupet.client.MemberServiceClientAsync
import com.ssafy.fluffitflupet.dto.*
import com.ssafy.fluffitflupet.entity.Flupet
import com.ssafy.fluffitflupet.entity.MemberFlupet
import com.ssafy.fluffitflupet.error.ErrorType
import com.ssafy.fluffitflupet.exception.CustomBadRequestException
import com.ssafy.fluffitflupet.repository.FlupetRepository
import com.ssafy.fluffitflupet.repository.MemberFlupetRepository
import com.ssafy.fluffitflupet.scheduler.AchaCalculator
import com.ssafy.fluffitflupet.scheduler.PetTaskScheduler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.system.measureTimeMillis

@Service
class FlupetService(
    private val memberFlupetRepository: MemberFlupetRepository,
    private val client: MemberServiceClientAsync,
    private val flupetRepository: FlupetRepository,
    private val env: Environment,
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val achaCalculator: AchaCalculator,
    private val petTaskScheduler: PetTaskScheduler
) {
    //lombok slf4j를 쓰기 위해
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    //우선 요청을 받으면 포만감과 건강의 업데이트 시간을 확인한 다음 최신 데이터를 업데이트를 하고 이후 그 flupet의 정보를 리턴
    suspend fun getMainInfo(userId: String): MainInfoResponse? = coroutineScope {
        println("여기왔나?")
        val mainInfoDto = async { memberFlupetRepository.findMainInfoByUserId(userId) }
        //ErrorDecoder로 오류 처리를 할지, 아니면 try~catch로 오류처리를 할지
        val coinWait = async { client.getUserCoin(userId) }
        log.info("여긴가??22")
        val dto = mainInfoDto.await()
        log.info("db조회 상태는 ${dto.toString()}")
        val coin = coinWait.await()
        log.info("coin은 ${coin}")
        if(dto == null){ //현재 플러펫이 없다(mainInfoDto 연산이 완료될때까지 '블로킹되지 않고' 기다리게 된다)
            val response = MainInfoResponse()
            response.coin = coin.coin
            return@coroutineScope response
        } else {
            var evolveAvailable = false
            if(dto.stage == 1){
                if(dto.exp >= 100){
                    evolveAvailable = true
                }
            }else if(dto.stage < (env.getProperty("total.stage")?.toInt() ?: 3)){
                if(dto.exp >= 300){
                    evolveAvailable = true
                }
            }
            val response = MainInfoResponse(
                fullness = if(dto.isDead) 0 else dto.fullness,
                health = if(dto.isDead) 0 else dto.health,
                flupetName = dto.flupetName,
                imageUrl = if(dto.isDead) listOf(env.getProperty("tombstone.img", "")) else dto.imageUrl.split(","),
                birthDay = dto.birthDay.toLocalDate().toString(),
                age = "${ChronoUnit.DAYS.between(dto.birthDay, LocalDateTime.now())}일 ${(ChronoUnit.HOURS.between(dto.birthDay, LocalDateTime.now())) % 24}시간",
                isEvolutionAvailable = evolveAvailable,
                nextFullnessUpdateTime = if(dto.isDead) 0 else (dto.nextFullnessUpdateTime.plusMinutes(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                nextHealthUpdateTime = if(dto.isDead) 0 else (dto.nextHealthUpdateTime.plusMinutes(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                coin = coin.coin
            )
            return@coroutineScope response
        }
    }

    suspend fun updateNickname(userId: String, nickname: String?){
        log.info("업데이트 닉네임")
        val rgx = Regex("^[가-힣a-zA-Z0-9]+$") //한글 범위, 영어 대소문자 범위 및 숫자 범위를 포함하는 정규 표현식
        if(nickname.isNullOrEmpty()){
            throw CustomBadRequestException(ErrorType.TOO_SHORT_NICKNAME)
        }else if(nickname.length > 8){
            throw CustomBadRequestException(ErrorType.TOO_LONG_NICKNAME)
        }else if(!rgx.matches(nickname)){
            throw CustomBadRequestException(ErrorType.WRONG_CONDITION)
        }else {
            val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
            if(mflupet == null){
                throw CustomBadRequestException(ErrorType.INVALID_USERID)
            }
            mflupet.name = nickname
            memberFlupetRepository.save(mflupet).awaitSingle()
        }
    }

    suspend fun getFullness(userId: String): FullResponse = coroutineScope {
        log.info("포만감 조회")
        val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
        if(mflupet == null){
            throw CustomBadRequestException(ErrorType.INVALID_USERID)
        }
        val petType = async(Dispatchers.IO){ flupetRepository.findById(mflupet.flupetId!!) }
        val beforeFull = mflupet.fullness
        withContext(Dispatchers.Default) { petTaskScheduler.updateFullness(mflupet) }
        if(mflupet.fullness <= 0){
            mflupet.isDead = true
            mflupet.endTime = LocalDateTime.now()
            withContext(Dispatchers.IO){ memberFlupetRepository.save(mflupet).awaitSingle() }
        }else if(mflupet.fullness != beforeFull){
            withContext(Dispatchers.Default){
                mflupet.achaTime = achaCalculator.calAchaTime(mflupet.fullness, mflupet.health) ?: mflupet.achaTime
            }
            withContext(Dispatchers.IO){ memberFlupetRepository.save(mflupet).awaitSingle() }
        }
        //withContext(Dispatchers.IO){ memberFlupetRepository.save(mflupet).awaitSingle() }
        var evolveAvailable = false
        val petTmp = petType.await()
        if(petTmp!!.stage == 1){
            if(mflupet.exp >= 100){
                evolveAvailable = true
            }
        }else if(petTmp.stage < (env.getProperty("total.stage")?.toInt() ?: 3)){
            if(mflupet.exp >= 300){
                evolveAvailable = true
            }
        }
        return@coroutineScope FullResponse(
            fullness = if(mflupet.fullness <= 0) 0 else mflupet.fullness,
            nextUpdateTime = (mflupet.fullnessUpdateTime!!.plusMinutes(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            isEvolutionAvailable = evolveAvailable
        )
    }

    suspend fun getHealth(userId: String): HealthResponse = coroutineScope {
        //시간 측정용(임시)-운영시에는 주석 처리
//        val measuredTime = measureTimeMillis {
//            withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
//        }
//        log.info("측정된 시간은 " + measuredTime.toString())

        val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
        if(mflupet == null){
            throw CustomBadRequestException(ErrorType.INVALID_USERID)
        }
        val petTypeAwait = async(Dispatchers.IO){ flupetRepository.findById(mflupet.flupetId!!) }
        val beforeHealth = mflupet.health
        withContext(Dispatchers.Default) { petTaskScheduler.updateHealth(mflupet) }
        if(mflupet.health <= 0){
            mflupet.isDead = true
            mflupet.endTime = LocalDateTime.now()
            withContext(Dispatchers.IO){ memberFlupetRepository.save(mflupet).awaitSingle() }
        }else if(mflupet.health != beforeHealth){
            withContext(Dispatchers.Default){
                mflupet.achaTime = achaCalculator.calAchaTime(mflupet.fullness, mflupet.health) ?: mflupet.achaTime
            }
            withContext(Dispatchers.IO){ memberFlupetRepository.save(mflupet).awaitSingle() }
        }
        //withContext(Dispatchers.IO){ memberFlupetRepository.save(mflupet).awaitSingle() }
        var evolveAvailable = false
        val petType = petTypeAwait.await()
        if(petType!!.stage == 1){
            if(mflupet.exp >= 100){
                evolveAvailable = true
            }
        }else if(petType.stage < (env.getProperty("total.stage")?.toInt() ?: 3)){
            if(mflupet.exp >= 300){
                evolveAvailable = true
            }
        }
        return@coroutineScope HealthResponse(
            health = mflupet.health,
            nextUpdateTime = (mflupet.healthUpdateTime!!.plusMinutes(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            isEvolutionAvailable = evolveAvailable
        )
    }

    suspend fun getPetCollection(userId: String): CollectionResponse {
        log.info("도감 조회")
        val flupets = withContext(Dispatchers.IO){ memberFlupetRepository.findFlupetsByUserId(userId).toList() }
        return CollectionResponse(flupets)
    }

    suspend fun generateFlupet(userId: String): GenFlupetResponse = coroutineScope {
        log.info("플러펫 생성")
        val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
        if(mflupet != null){
            throw CustomBadRequestException(ErrorType.NOT_AVAILABLE_GEN_PET)
        }
        val achaTime = withContext(Dispatchers.Default) { achaCalculator.calAchaTime(100, 100) }
        log.info("계산된 achaTime은 $achaTime")
        launch(Dispatchers.IO) {
            memberFlupetRepository.save(
                MemberFlupet(
                    flupetId = 1,
                    memberId = userId,
                    name = "새로운 알",
                    achaTime = achaTime
                )
            ).awaitSingle()
        }
        val fInfo = async(Dispatchers.IO) { flupetRepository.findById(1) }
        return@coroutineScope GenFlupetResponse(
            flupetName = "새로운 알",
            imageUrl = fInfo.await()!!.imgUrl.split(","),
            fullness = 100,
            health = 100,
            nextHealthUpdateTime = (LocalDateTime.now().plusHours(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            nextFullnessUpdateTime = (LocalDateTime.now().plusHours(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }

    @Transactional
    suspend fun evolveFlupet(userId: String): EvolveResponse = coroutineScope {
        log.info("진화하기")
        val mypet = async(Dispatchers.IO) { memberFlupetRepository.findByMemberIdAndFlupet(userId) }
        val mflupet = async(Dispatchers.IO) { memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
        val mypetRst = mypet.await() ?: throw CustomBadRequestException(ErrorType.INVALID_USERID)

        //val t = flupetRepository.findByStage(mypetRst.stage+1).toList()
        log.info("전체 진화 단계 수는 ${env.getProperty("total.stage")}")
        val flupets = async(Dispatchers.IO) {
            if(mypetRst.stage == 1){ //기본 단계인 알을 1단계로 설정(알에서 그 다음으로 진화할때는 있는 캐릭터 중 랜덤으로 설정)
                flupetRepository.findByStage(mypetRst.stage+1).toList()
            }else if(mypetRst.stage < (env.getProperty("total.stage")?.toInt() ?: 3)){
                flupetRepository.findById(mypetRst.flupetId+1)
            }else{
                throw CustomBadRequestException(ErrorType.NOT_AVAILABLE_EVOLVE)
            }
            //flupetRepository.findByStage(mypetRst.stage).toList()
        }

        //진화하기전의 기존의 펫을 죽었다고 처리
        //mflupet.await()이 null이면 예외 처리
        val mflupetRst = mflupet.await() ?: throw CustomBadRequestException(ErrorType.INVALID_USERID)
        mflupetRst.isDead = true
        mflupetRst.endTime = LocalDateTime.now()
        launch(Dispatchers.IO) {
            memberFlupetRepository.save(mflupetRst).awaitSingle()
        }

        var flist: List<Flupet> = listOf() //stage == 1일때 사용
        var evolveFlupet: Flupet? = null //stage == 2일때 사용
        val anyrst = flupets.await() ?: throw CustomBadRequestException(ErrorType.NOT_FOUND_NEXT_FLUPET)
        if(mypetRst.stage == 1){
            val tmp = anyrst as List<Flupet>
            flist = tmp.shuffled() //리스트(List<Flupet>)를 무작위로 썩은 값을 반환한다.
        }else{
            evolveFlupet = anyrst as Flupet?
        }
        //진화된 새로운 캐릭터(펫)을 지정해서 저장한다.
        launch(Dispatchers.IO) {
            val mftmp = memberFlupetRepository.save(
                MemberFlupet(
                    flupetId = if(mypetRst.stage == 1) flist[0].id else evolveFlupet!!.id,
                    memberId = userId,
                    name = mflupetRst.name,
                    exp = mflupetRst.exp,
                    steps = mflupetRst.steps,
                    createTime = mflupetRst.createTime,
                    fullness = mflupetRst.fullness,
                    health = mflupetRst.health,
                    patCnt = mflupetRst.patCnt,
                    achaTime = mflupetRst.achaTime,
                    fullnessUpdateTime = mflupetRst.fullnessUpdateTime,
                    healthUpdateTime = mflupetRst.healthUpdateTime
                )
            ).awaitSingle()

            //왜 @CreatedDate가 붙어있는 필드가 null이 아닐경우에도 할당한 값이 무시되고 현재 시간으로 설정이 되지??
            mftmp.createTime = mflupetRst.createTime
            mftmp.fullnessUpdateTime = mflupetRst.fullnessUpdateTime
            mftmp.healthUpdateTime = mflupetRst.healthUpdateTime
            memberFlupetRepository.save(mftmp).awaitSingle()
        }
        return@coroutineScope EvolveResponse(
            flupetName = mflupetRst.name,
            imageUrl = if(mypetRst.stage == 1) flist[0].imgUrl.split(",") else evolveFlupet!!.imgUrl.split(","),
            fullness = mflupetRst.fullness,
            health = mflupetRst.health,
            isEvolutionAvailable = false,
            nextFullnessUpdateTime = (mflupetRst.fullnessUpdateTime!!.plusHours(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            nextHealthUpdateTime = (mflupetRst.healthUpdateTime!!.plusHours(2)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }

    suspend fun getMyPetHistory(userId: String): HistoryResponse = coroutineScope {
        log.info("펫 키웠던 내역 조회")
        val mflupetWait = async(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
        val flupetsWait = async(Dispatchers.IO){
            memberFlupetRepository.findHistoryByUserId(userId)
                .toList()
                .distinctBy { it.birthDay }
        }

        val mflupet = mflupetWait.await()
        val flupets = flupetsWait.await()
        var flag = false
        if(flupets.isNotEmpty() && mflupet != null){
            flag = flupets[0].birthDay.isEqual(mflupet.createTime)
        }
        var tmp: ArrayList<HistoryResponse.MyPet> = arrayListOf()
        flupets.map { value ->
            tmp.add(
                HistoryResponse.MyPet(
                    species = value.species,
                    name = value.name,
                    imageUrl = value.imageUrl,
                    birthDay = value.birthDay.toLocalDate(),
                    endDay = value.endDay,
                    age = value.age,
                    steps = value.steps
                )
            )
        }
        return@coroutineScope HistoryResponse(if(flupets.isEmpty()) tmp else tmp.subList(if(flag) 1 else 0, tmp.size))
    }

    suspend fun getFlupetRank(userId: String): RankingResponse = coroutineScope {
        //throw CustomBadRequestException(ErrorType.TEST_EXCEPTION)
        val nick = async { client.getNickname(userId) }
        var nick1: Any? = null; var nick2: Any? = null; var nick3: Any? = null; var nick4: Any? = null
        var flag = 0
        val mrank = ArrayList<RankDto>()
        val rank = withContext(Dispatchers.IO){ memberFlupetRepository.findFlupetRank(userId) }
        rank.collect{ value ->
            log.info("flag값은 $flag")
            log.info("요청 userId는 ${value.userId}")
            val tmp = async { client.getNickname(value.userId) }
//            when(flag){
//                0 -> nick1 = async { client.getNickname(value.userId) } //나
//                1 -> nick2 = async { client.getNickname(value.userId) } //1등
//                2 -> nick3 = async { client.getNickname(value.userId) } //2등
//                3 -> nick4 = async { client.getNickname(value.userId) } //3등 -> 내 순위에 따라서 4등이 될 수도 있다.
//            }
            value.userNickname = tmp.await().nickname
            mrank.add(value)
            flag++
        }

        if(mrank.isEmpty()){
            throw CustomBadRequestException(ErrorType.INVALID_USERID)
        }
        if(mrank[0].rank in 1..3){ //내 순위가 1~3위안에 포함되어 있다.(nick4 요청을 취소시킬 수 있다.)
            if(mrank.size == 4){
//                (nick4 as Deferred<Nick>).cancel()
//                mrank[0].userNickname = (nick1 as Deferred<Nick>).await().nickname
                mrank.removeAt(mrank.size-1)
                if(mrank[0].rank == 2) { //내가 2등
                    val tmp = mrank[0]
                    mrank.add(2, tmp)
//                    mrank[1].userNickname = (nick2 as Deferred<Nick>).await().nickname //1등
//                    mrank[3].userNickname = (nick3 as Deferred<Nick>).await().nickname //3등
                }else if(mrank[0].rank == 3) { //내가 3등
                    val tmp = mrank[0]
                    mrank.add(tmp)
//                    mrank[1].userNickname = (nick2 as Deferred<Nick>).await().nickname //1등
//                    mrank[2].userNickname = (nick3 as Deferred<Nick>).await().nickname //2등
                }else{ //내가 1등이다.
                    val tmp = mrank[0]
                    mrank.add(1, tmp)
//                    mrank[2].userNickname = (nick2 as Deferred<Nick>).await().nickname //2등
//                    mrank[3].userNickname = (nick3 as Deferred<Nick>).await().nickname //3등
                }
            }else{ //현재 살아있는 펫이 4명이 안된다.
                if(mrank[0].rank == mrank.size){
                    mrank.add(mrank[0])
                }else{
                    mrank.add(mrank[0].rank, mrank[0])
                }
            }
        }else{
//            mrank[0].userNickname = (nick1 as Deferred<Nick>).await().nickname
//            mrank[1].userNickname = (nick2 as Deferred<Nick>).await().nickname //1등
//            mrank[2].userNickname = (nick3 as Deferred<Nick>).await().nickname //2등
//            mrank[3].userNickname = (nick4 as Deferred<Nick>).await().nickname //3등
        }

        var tmprank = mrank.removeAt(0)
        var myRank = RankingResponse.PetAgeInfo(
            rank = tmprank.rank,
            userNickname = tmprank.userNickname,
            lifetime = tmprank.lifetime,
            flupetNickname = tmprank.flupetNickname,
            imageUrl = tmprank.imageUrl
        )
        var ranking = mrank.map { value ->
            RankingResponse.PetAgeInfo(
                rank = value.rank,
                userNickname = value.userNickname,
                lifetime = value.lifetime,
                flupetNickname = value.flupetNickname,
                imageUrl = value.imageUrl
            )
        }

        return@coroutineScope RankingResponse(
            ranking = ranking,
            myRank = myRank,
        )
    }

    suspend fun getFlupetBattleRank(memberIds: List<String>): List<RankFlupetInfoDto> = coroutineScope {
        if(memberIds.size == 4){
            // 만약 랭킹에 올라있는 사용자의 펫이 죽어있다면 가장 최근 펫을 조회하기 위해
            val info1 = async { memberFlupetRepository.findMainInfoByUserId(memberIds[0]) }
            val info2 = async { memberFlupetRepository.findMainInfoByUserId(memberIds[1]) }
            val info3 = async { memberFlupetRepository.findMainInfoByUserId(memberIds[2]) }
            val info4 = async { memberFlupetRepository.findMainInfoByUserId(memberIds[3]) }

            val infolist = arrayListOf(
                RankFlupetInfoDto(flupetNickname = info1.await()?.flupetName ?: "",
                    flupetImageUrl = info1.await()?.imageUrl?.split(",") ?: listOf("")),
                RankFlupetInfoDto(flupetNickname = info2.await()?.flupetName ?: "",
                    flupetImageUrl = info2.await()?.imageUrl?.split(",") ?: listOf("")),
                RankFlupetInfoDto(flupetNickname = info3.await()?.flupetName ?: "",
                    flupetImageUrl = info3.await()?.imageUrl?.split(",") ?: listOf("")),
                RankFlupetInfoDto(flupetNickname = info4.await()?.flupetName ?: "",
                    flupetImageUrl = info4.await()?.imageUrl?.split(",") ?: listOf(""))
            )
            return@coroutineScope infolist
        }else{
            val list = ArrayList<RankFlupetInfoDto>()
            for(id in memberIds){
                val infoDto = memberFlupetRepository.findMainInfoByUserId(id)
                list.add(
                    RankFlupetInfoDto(
                        flupetNickname = infoDto?.flupetName ?: "",
                        flupetImageUrl = infoDto?.imageUrl?.split(",") ?: listOf("")
                    )
                )
            }
            return@coroutineScope list
        }
    }

    suspend fun patMyFlupet(userId: String) = coroutineScope {
        log.info("쓰다듬기")
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val patTime = reactiveRedisTemplate.opsForValue().get("patTime: $userId").awaitSingleOrNull()
        if(patTime != null){ //오늘 쓰다듬기를 한적이 있다.
            var parseTime = LocalDateTime.parse(patTime, formatter)
            if(LocalDateTime.now().isBefore(parseTime.plusMinutes(5))){ //마지막으로 쓰다듬기를 한 후 5분이 지나지 않았다.
                throw CustomBadRequestException(ErrorType.PAT_TIME_LIMIT)
            }
            var mf = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
            if(mf == null){
                throw CustomBadRequestException(ErrorType.INVALID_USERID)
            }
            if(mf.patCnt == 0){
                throw CustomBadRequestException(ErrorType.PAT_CNT_LIMIT)
            }
            mf.patCnt--
            mf.exp += 1
            launch(Dispatchers.IO) { reactiveRedisTemplate.opsForValue().set("patTime: $userId", LocalDateTime.now().toString()).awaitSingle() }
            launch(Dispatchers.IO) { memberFlupetRepository.save(mf).awaitSingle() }
        }else{
            var mf = withContext(Dispatchers.IO){ memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
            if(mf == null){
                throw CustomBadRequestException(ErrorType.INVALID_USERID)
            }
            mf.patCnt--
            mf.exp += 1
            launch(Dispatchers.IO) { reactiveRedisTemplate.opsForValue().set("patTime: $userId", LocalDateTime.now().toString()).awaitSingle() }
            launch(Dispatchers.IO) { memberFlupetRepository.save(mf).awaitSingle() }
        }
    }

    suspend fun getBattleInfo(userId: String): BattleInfoDto {
        log.info("battle-service에서 요청 들어왔다.")
        val mflupet = withContext(Dispatchers.IO){ memberFlupetRepository.findBattleInfo(userId) }
        if(mflupet == null){
            return BattleInfoDto()
        }
        return mflupet
    }
}