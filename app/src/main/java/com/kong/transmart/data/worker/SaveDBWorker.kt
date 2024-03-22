package com.kong.transmart.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kong.transmart.data.repository.BankRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SaveDBWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val bankRepository: BankRepository
): CoroutineWorker(appContext, params) {
    private val TAG = SaveDBWorker::class.simpleName
    override suspend fun doWork(): Result {
        val banks = bankRepository.getBanks()
        Log.d(TAG, "Banks: ${banks.first()}")

        return Result.success()
    }
}