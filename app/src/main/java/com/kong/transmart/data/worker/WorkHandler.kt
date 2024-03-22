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
    private val TAG_SCRAPER = "SCRAPER"

    fun scheduleDailyWork() {
        // TODO change request to periodic request
        val scrape = OneTimeWorkRequestBuilder<ScrapeWorker>()
            .addTag(TAG_SCRAPER)
            .build()

        val saveDB = OneTimeWorkRequestBuilder<SaveDBWorker>()
            .build()
        workManager.enqueue(saveDB)
    }
}