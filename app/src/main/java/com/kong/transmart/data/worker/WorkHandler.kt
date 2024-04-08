package com.kong.transmart.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kong.transmart.util.DateUtils
import com.kong.transmart.util.NotificationUtils
import java.util.concurrent.TimeUnit

class WorkHandler(val context: Context) {
    private val TAG = WorkHandler::class.simpleName
    private val workManager = WorkManager.getInstance(context)

    private fun dailySyncWork() {
            val initialDelay = DateUtils.calculateDelayUntil(8, 0)
        Log.d(TAG, "Add scheduled work: initial delay: $initialDelay")

        val syncRequest = PeriodicWorkRequestBuilder<ExchangeRateSyncWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WorkConstants.TAG_SYNC_EXCHANGE_RATE,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    private fun testScheduler() {
        val initialDelay = DateUtils.calculateDelayUntil(10, 30)
        Log.d(TAG, "Add scheduled work: initial delay: $initialDelay")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<ExchangeRateSyncWorker>(15, TimeUnit.MINUTES)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WorkConstants.TAG_TEST,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    private fun removeWorks() {
        workManager.cancelUniqueWork(WorkConstants.TAG_SYNC_EXCHANGE_RATE)
        workManager.cancelUniqueWork(WorkConstants.TAG_TEST)
    }

     fun scheduleDailyWork() {
//         removeWorks()
         NotificationUtils.createNotificationChannels(context)
         dailySyncWork()
//         testScheduler()

    }

}

object WorkConstants {
    const val TAG_SYNC_EXCHANGE_RATE = "TAG_SYNC_EXCHANGE_RATE"
    const val TAG_TEST = "TAG_TEST"
}