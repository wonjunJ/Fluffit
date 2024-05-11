package com.ssafy.fluffitflupet.scheduler

import com.ssafy.fluffitflupet.entity.MemberFlupet
import com.ssafy.fluffitflupet.repository.MemberFlupetRepository
import com.ssafy.fluffitflupet.service.FlupetService
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.BufferOverflowStrategy
import reactor.core.scheduler.Schedulers
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.coroutines.CoroutineContext

@Component
class PetTaskScheduler(
    private val memberFlupetRepository: MemberFlupetRepository
): CoroutineScope { //CoroutineScope를 컴포넌트 레벨에서 구현하여 각 스케쥴된 작업이 자신의 CoroutineScope를 가지게 된다.
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job
    private val log = LoggerFactory.getLogger(FlupetService::class.java)
    var dropList = ArrayList<MemberFlupet>()

    //2시간마다 스케쥴링을 돈다(초기 딜레이를 얼마로 할지)
    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60 * 2L)
    fun run() {
        log.info("스케쥴링")
        memberFlupetRepository.findAllByIsDeadIsFalse()
            .onBackpressureBuffer(256,
                {dropped -> dropList.add(dropped)},
                BufferOverflowStrategy.DROP_LATEST)
            .publishOn(Schedulers.parallel(), false, 32)
            .subscribe(this::handleData)
            { error -> log.error(error.toString()) }

        for( memberFlupet in dropList ) {
            val job1 = launch { updateFullness(memberFlupet) }
            val job2 = launch { updateHealth(memberFlupet) }
            launch {
                joinAll(job1, job2)
                if(memberFlupet.fullness == 0 || memberFlupet.health == 0) {
                    memberFlupet.isDead = true
                    memberFlupet.endTime = LocalDateTime.now()
                }
                withContext(Dispatchers.IO){
                    memberFlupetRepository.save(memberFlupet)
                }
            }
        }
    }

    fun handleData(data: MemberFlupet){
        log.info("handleData")
        //포만감 업데이트
        val job1 = launch { updateFullness(data) }
        //건강 업데이트
        val job2 = launch { updateHealth(data) }
        launch {
            joinAll(job1, job2)
            if(data.fullness == 0 || data.health == 0){
                data.isDead = true
                data.endTime = LocalDateTime.now()
            }
            log.info("스케쥴링에서의 현재 포만감은 ${data.fullness}")
            withContext(Dispatchers.IO){
                memberFlupetRepository.save(data)
            }
        }
    }

    suspend fun updateFullness(data: MemberFlupet) {
        var hoursDiff = ChronoUnit.HOURS.between(data.fullnessUpdateTime, LocalDateTime.now())
        var tmp = 0
        var fullness = data.fullness
//        for (hour in 1..hoursDiff step 1) {
//            val checkTime = data.fullnessUpdateTime?.plusHours(hour)
//            if (checkTime?.hour in 0..7) {
//                if ((checkTime?.hour?.minus(tmp) ?: 0) >= 2) {
//                    fullness -= 5
//                    tmp += 2
//                }
//            } else {
//                fullness -= 5
//            }
//        }
        fullness -= 3
        data.fullness = fullness
        data.fullnessUpdateTime = LocalDateTime.now()
    }

    suspend fun updateHealth(data: MemberFlupet) {
        var hoursDiff = ChronoUnit.HOURS.between(data.healthUpdateTime, LocalDateTime.now())
        var tmp = 0
        var health = data.health
        for (hour in 1..hoursDiff step 1) {
            val checkTime = data.healthUpdateTime?.plusHours(hour)
            if (checkTime?.hour in 0..7) {
                if ((checkTime?.hour?.minus(tmp) ?: 0) >= 2) {
                    health -= 3
                    tmp += 2
                }
            } else {
                health -= 3
            }
        }
        data.health = health
        data.healthUpdateTime = LocalDateTime.now()
    }

    @PreDestroy
    fun destroy() {
        job.cancel()  // 모든 코루틴을 취소
        println("CoroutineScheduledTasks 빈 파괴 시 모든 코루틴 취소")
    }
}