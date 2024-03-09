package com.kong.transmart.repositories

import android.util.Log
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.kong.transmart.BuildConfig
import com.kong.transmart.database.ExchangeRateDAO
import com.kong.transmart.models.ExchangeRateEntity
import com.kong.transmart.network.CurrencyRateApi
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ExchangeRateRepository(
    private val exchangeRateDao: ExchangeRateDAO,
    private val currencyRateApi: CurrencyRateApi) {
    private val TAG = ExchangeRateRepository::class.simpleName

    // TODO Remove test function
    suspend fun test(searchDate: String) {
        try {
            val response = currencyRateApi.getExchangeRate(BuildConfig.API_KEY, searchDate, "AP01")
            if (response?.isNotEmpty() == true && response[0].result == 1) {
                Log.d(TAG, response.toString())
                val exchangeRate = response.find {it.cur_unit == "CAD"}
                Log.d(TAG, "Open API Exchange rate: ${exchangeRate?.deal_bas_r}")
            } else {
                Log.e(TAG, "Error while fetching exchange rate: response is empty")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while fetching exchange rate ${e.message}")
        }
    }
    suspend fun addExchangeRate(exchangeRate: ExchangeRateEntity) {
        exchangeRateDao.addExchangeRate(exchangeRate)
    }

    suspend fun updateExchangeRate(exchangeRate: ExchangeRateEntity) {
        exchangeRateDao.updateExchangeRate(exchangeRate)
    }

    suspend fun deleteExchangeRate(exchangeRate: ExchangeRateEntity) {
        exchangeRateDao.deleteExchangeRate(exchangeRate)
    }

    suspend fun deleteAllExchangeRates() {
        exchangeRateDao.deleteAllExchangeRates()
    }

    fun getAllExchangeRates(): Flow<List<ExchangeRateEntity>> {
        return exchangeRateDao.getAllExchangeRates()
    }

    fun getMostRecentDate(): Flow<Date> {
        return exchangeRateDao.getMostRecentDate()
    }

    fun getExchangeRateByDate(date: Date): Flow<ExchangeRateEntity> {
        return exchangeRateDao.getExchangeRateByDate(date)
    }

    fun getExchangeRatesForLastWeek(lastWeek: Date): Flow<List<ExchangeRateEntity>> {
        return exchangeRateDao.getExchangeRatesForLastWeek(lastWeek)
    }

    fun getExchangeRatesForLastMonth(lastMonth: Date): Flow<List<ExchangeRateEntity>> {
        return exchangeRateDao.getExchangeRatesForLastMonth(lastMonth)
    }
}