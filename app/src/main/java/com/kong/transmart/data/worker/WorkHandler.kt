package com.kong.transmart.data.worker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager

class WorkHandler(context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleDailyWork() {
        // TODO change request to periodic request


        val scrape = OneTimeWorkRequestBuilder<ScrapeWorker>()
            .addTag(WorkConstants.TAG_SCRAPER)
            .build()

        val saveDB = OneTimeWorkRequestBuilder<SaveDBWorker>()
            .addTag(WorkConstants.TAG_SAVE_DB)
            .build()

        workManager.beginWith(scrape)
            .then(saveDB)
            .enqueue()
    }
}

object WorkConstants {
    const val TAG_SCRAPER = "SCRAPER"
    const val TAG_SAVE_DB = "SAVE_DB"
    const val KEY_RATE = "SCRAPE_WORKER_RATE"
    const val KEY_DATE = "SCRAPE_WORKER_DATE"
}