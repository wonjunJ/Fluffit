package com.kiwa.fluffit.home

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class StatUpdateWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}
