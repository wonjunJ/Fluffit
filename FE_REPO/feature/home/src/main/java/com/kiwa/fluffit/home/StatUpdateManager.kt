package com.kiwa.fluffit.home

import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

internal fun WorkManager.reserveNewRequest(tag: String, nextUpdateTime: Long) {
    val fullnessUpdateRequest = OneTimeWorkRequest.Builder(StatUpdateWorker::class.java)
        .setConstraints(getTimeConstraints(nextUpdateTime))
        .addTag(tag)
        .build()
    this.cancelAllWorkByTag(tag)
    this.enqueue(fullnessUpdateRequest)
}

internal fun getTimeConstraints(nextUpdateTime: Long): Constraints =
    Constraints.Builder()
        .setTriggerContentUpdateDelay(
            nextUpdateTime - System.currentTimeMillis(),
            java.util.concurrent.TimeUnit.MILLISECONDS
        )
        .build()

