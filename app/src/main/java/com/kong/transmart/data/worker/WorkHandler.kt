package com.kong.transmart.data.worker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager

class WorkHandler() {
    // TODO change getInstance() to getInstance(context)
    private val workManager = WorkManager.getInstance()
    private val TAG_SCRAPER = "SCRAPER"

    fun scheduleDailyWork() {
        // TODO change request to periodic request
        val scrape = OneTimeWorkRequestBuilder<ScrapeWorker>()
            .addTag(TAG_SCRAPER)
            .build()
        workManager.enqueue(scrape)

    }
}