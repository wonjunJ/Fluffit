package com.kiwa.fluffit.home

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kiwa.domain.repository.MainRepository
import com.kiwa.domain.usecase.GetMainUIInfoUseCase
import com.kiwa.domain.usecase.UpdateFullnessUseCase
import dagger.assisted.AssistedInject
import javax.inject.Inject

class StatUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private lateinit var updateFullnessUseCase: UpdateFullnessUseCase

    init {
//        Log.d("확인", "유즈케이스: $updateFullnessUseCase")

    }

    override suspend fun doWork(): Result {
        val stat = inputData.getString("stat")
        when (stat) {
            "fullness" -> {
                Log.d("확인", "작업 실행")
                updateFullnessUseCase()
            }

            "health" -> {
                // mainRepository.updateHealth()
            }

            else -> {}
        }
        return Result.success()
    }
}
