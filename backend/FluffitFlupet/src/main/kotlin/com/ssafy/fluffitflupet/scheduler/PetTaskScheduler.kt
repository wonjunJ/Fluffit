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
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.time.temporal.ChronoUnit
import kotlin.coroutines.CoroutineContext

@Component
class PetTaskScheduler(
    private val memberFlupetRepository: MemberFlupetRepository
): CoroutineScope { //CoroutineScope를 컴포넌트 레벨에서 구현하여 각 스케쥴된 작업이 자신의 CoroutineScope를 가지게 된다.
    private val job = Job()
    private val log = LoggerFactory.getLogger(FlupetService::class.java)
    var dropList = ArrayList<MemberFlupet>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    //2시간마다 스케쥴링을 돈다
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 2)
    fun run() {
        memberFlupetRepository.findAllByDeadIsFalse()
            .onBackpressureBuffer(256,
                {dropped -> dropList.add(dropped)},
                BufferOverflowStrategy.DROP_LATEST)
            .publishOn(Schedulers.parallel(), false, 32)
            .subscribe({data -> },
                { error ->  log.error(error.toString())})
//        launch {
//            println("Coroutine context에서 비동기 작업 실행: ${System.currentTimeMillis()}")
//        }
    }

    suspend fun handleData(data: MemberFlupet){
        coroutineScope {
            //포만감 업데이트
            launch {
                var hoursDiff = ChronoUnit.HOURS.between(data.fullnessUpdateTime, LocalDateTime.now())
                var tmp = 0
                var fullness = data.fullness
                var flupet = MemberFlupet(id = data.id, name = data.name)
                for(hour in 1..hoursDiff step 1){
                    val checkTime = data.fullnessUpdateTime?.plusHours(hour)
                    if(checkTime?.hour in 0..7){ //00시부터 8시 사이
                        if((checkTime?.hour?.minus(tmp) ?: 0) >= 2){
                            fullness -= 5
                            tmp += 2
                        }
                    }else{
                        fullness -= 5
                    }
                }
                data.fullness = fullness
                memberFlupetRepository.save(data)
            }
            //건강 업데이트
            launch {

            }
        }
    }

    @PreDestroy
    fun destroy() {
        job.cancel()  // 모든 코루틴을 취소
        println("CoroutineScheduledTasks 빈 파괴 시 모든 코루틴 취소")
    }
}