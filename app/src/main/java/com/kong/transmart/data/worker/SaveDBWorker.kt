package com.kong.transmart.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kong.transmart.data.repository.ExchangeRateRepository
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.util.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SaveDBWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val exchangeRateRepository: ExchangeRateRepository
): CoroutineWorker(appContext, params) {
    private val TAG = SaveDBWorker::class.simpleName
    override suspend fun doWork(): Result {
        val rate = inputData.getDouble(WorkConstants.KEY_RATE, 0.0)
        val date = inputData.getString(WorkConstants.KEY_DATE)

        if (rate != 0.0) {
            exchangeRateRepository.updateOrInsertExchangeRate(
                ExchangeRateEntity(rate = rate, date = DateUtils.stringToDate(date!!)))
            Log.d(TAG, "rate: $rate, date: $date")
        }

        return Result.success()
    }
}