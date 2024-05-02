package com.ssafy.fluffitflupet.scheduler

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.coroutines.CoroutineContext

@Component
class PetTaskScheduler: CoroutineScope { //CoroutineScope를 컴포넌트 레벨에서 구현하여 각 스케쥴된 작업이 자신의 CoroutineScope를 가지게 된다.
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    @Scheduled(fixedRate = 5000)
    fun performAsyncTask() {
        launch {
            println("Coroutine context에서 비동기 작업 실행: ${System.currentTimeMillis()}")
        }
    }

    @PreDestroy
    fun destroy() {
        job.cancel()  // 모든 코루틴을 취소
        println("CoroutineScheduledTasks 빈 파괴 시 모든 코루틴 취소")
    }
}