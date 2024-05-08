package com.kiwa.fluffit.home

import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

internal fun WorkManager.enqueueNewRequest(tag: String, nextUpdateTime: Long) {
    val statData = Data.Builder().putString("stat", tag).build()
    val updateRequest = OneTimeWorkRequest.Builder(StatUpdateWorker::class.java)
        .setConstraints(getTimeConstraints(nextUpdateTime))
        .addTag(tag).setInputData(statData)
        .build()
    this.cancelAllWorkByTag(tag)
    this.enqueue(updateRequest)
    Log.d("확인", "등록 $tag")
}

internal fun getTimeConstraints(nextUpdateTime: Long): Constraints {
    Log.d("확인", "지연시간 ${nextUpdateTime - System.currentTimeMillis()}")
    return Constraints.Builder()
        .setTriggerContentUpdateDelay(
            nextUpdateTime - System.currentTimeMillis(),
            java.util.concurrent.TimeUnit.MILLISECONDS
        ).setRequiresCharging(false).setRequiresBatteryNotLow(false).build()
}

