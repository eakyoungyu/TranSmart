package com.kong.transmart.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.kong.transmart.data.remote.CurrencyRateScraper
import com.kong.transmart.util.DateUtils

class ScrapeWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    private val TAG = ScrapeWorker::class.simpleName

    override suspend fun doWork(): Result {
        val scraper = CurrencyRateScraper()
        val output = scraper.fetchCurrencyRate()
        val date = DateUtils.getToday()

        val outputData = workDataOf(
            WorkConstants.KEY_RATE to output.currencyRate,
            WorkConstants.KEY_DATE to DateUtils.dateToString(date)
        )

        Log.d(TAG, "${output.currencyRate} ${DateUtils.dateToString(date)}")

        return Result.success(outputData)
    }

}