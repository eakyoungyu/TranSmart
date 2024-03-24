package com.kong.transmart.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kong.transmart.data.remote.CurrencyRateScraper
import com.kong.transmart.data.repository.ExchangeRateRepository
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.util.DateUtils
import com.kong.transmart.util.NotificationUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
@HiltWorker
class ExchangeRateSyncWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted params: WorkerParameters,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val currencyRateScraper: CurrencyRateScraper
): CoroutineWorker(appContext, params) {
    private val TAG = ExchangeRateSyncWorker::class.simpleName
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Start work")
            val exchangeRateEntity = scrape()
            if (exchangeRateEntity.rate == 0.0) {
                throw Exception("Scraping failed")
            }

            saveDB(exchangeRateEntity)

            NotificationUtils.sendLowPriceAlertNotification(appContext, "Transmart",
                "Scraping done ${exchangeRateEntity.rate} ${DateUtils.dateToString(exchangeRateEntity.date)}")

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun scrape(): ExchangeRateEntity {
        Log.d(TAG, "scrape")
        val output = currencyRateScraper.fetchCurrencyRate()
        val date = DateUtils.getToday()
        val exchangeRateEntity = ExchangeRateEntity(rate = output.currencyRate, date = date)

        Log.d(TAG, "${output.currencyRate} ${DateUtils.dateToString(date)}")
        return exchangeRateEntity
    }
    private suspend fun saveDB(exchangeRateEntity: ExchangeRateEntity) {
        Log.d(TAG, "saveDB")
        exchangeRateRepository.updateOrInsertExchangeRate(exchangeRateEntity)
    }
}