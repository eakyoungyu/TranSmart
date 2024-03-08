package com.kong.transmart.repositories

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.kong.transmart.database.ExchangeRateDAO
import com.kong.transmart.models.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ExchangeRateRepository(private val exchangeRateDao: ExchangeRateDAO) {
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