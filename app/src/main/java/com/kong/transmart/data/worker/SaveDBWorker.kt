package com.kong.transmart.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


// TODO Use Hilt
class SaveDBWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return Result.success()
    }
}