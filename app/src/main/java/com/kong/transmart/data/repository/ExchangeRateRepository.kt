package com.kong.transmart.data.repository

import android.util.Log
import com.kong.transmart.data.csv.CsvParser
import com.kong.transmart.data.local.ExchangeRateDAO
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.model.ExchangeRateResponse
import com.kong.transmart.data.remote.CurrencyRateApi
import com.kong.transmart.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.Date

class ExchangeRateRepository(
    private val exchangeRateDao: ExchangeRateDAO,
    private val currencyRateApi: CurrencyRateApi,
    private val csvParser: CsvParser
) {
    private val TAG = ExchangeRateRepository::class.simpleName

    suspend fun loadFromCsv(){
        // if database is empty, add exchange rate from csv
        exchangeRateDao.getMostRecentDate().first().let { date ->
            if (date == null) {
                val parsedRates = csvParser.parseCsv()
                val sortedDates = parsedRates.toList().sortedBy { DateUtils.stringToDate(it.first) }
                // set previous date's rate to if the date is not in the csv
                val filledRates = sortedDates.fold(mutableListOf<ExchangeRateEntity>()) { acc, currentDate ->
                    val curDate = DateUtils.stringToDate(currentDate.first)
                    if (acc.isNotEmpty()) {
                        val prevDate = acc.last().date
                        var nextDate = DateUtils.getNextDate(prevDate)

                        while (nextDate.before(curDate)) {
                            val prevRate = acc.last().rate
                            acc.add(ExchangeRateEntity(rate = prevRate, date = nextDate))
                            nextDate = DateUtils.getNextDate(nextDate)
                        }
                    }
                    val curRate = currentDate.second.replace(",", "").toDouble()
                    acc.add(ExchangeRateEntity(rate = curRate, date = curDate))
                    acc
                }

                filledRates.forEach {
                    exchangeRateDao.addExchangeRate(it)
                }
            }
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

    suspend fun getExchangeRateByDate(date: Date): Flow<ExchangeRateEntity> {
        val exchangeRateFromDB = exchangeRateDao.getExchangeRateByDate(date).first()
        if (exchangeRateFromDB != null) {
            return flow { emit(exchangeRateFromDB) }
        }

        getExchangeRateFromApi(date)?.let {
            exchangeRateDao.addExchangeRate(it)
            return exchangeRateDao.getExchangeRateByDate(date)
        }

        return flow { emit(exchangeRateFromDB) }
    }

    private fun getCADRateFromResponse(response: List<ExchangeRateResponse>): Double? {
        Log.d(TAG, response.find{ it.cur_unit == "CAD" }.toString())
        return response.find { it.cur_unit == "CAD" }?.deal_bas_r?.toDouble()
    }
    private suspend fun getExchangeRateFromApi(date: Date): ExchangeRateEntity? {
        Log.d(TAG, "Fetching exchange rate for ${DateUtils.dateToString(date)}")
        try {
            val response = currencyRateApi.getExchangeRate(searchdate = DateUtils.dateToString(date))

            if (response.isNullOrEmpty()) {
                val prevExchangeRate = getExchangeRateByDate(DateUtils.getPreviousDate(date)).first()
                return ExchangeRateEntity(rate = prevExchangeRate.rate, date = date)
            }

            if (response[0].result != 1)
                throw Exception("Invalid response: result code ${response[0].result}")

            getCADRateFromResponse(response)?.let {
                return ExchangeRateEntity(rate = it, date = date)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while fetching exchange rate ${e.message}")
        }
        return null
    }

    // TODO update today's rate from 11:00 am to 8:00 pm
    suspend fun getExchangeRatesForLastWeek(): Flow<List<ExchangeRateEntity>>  {
        val dates = DateUtils.getLastWeekDatesToToday()
        val lastWeekRates = exchangeRateDao.getExchangeRatesForLastWeek(dates[0]).first()
        Log.d(TAG, "Last week dates: $dates")
        Log.d(TAG, "Last week rates: $lastWeekRates")

        return if (lastWeekRates.size == dates.size) {
            flow { emit(lastWeekRates) }
        } else {
            dates.forEach { date ->
                if (lastWeekRates.none { rate -> DateUtils.isSameDay(date, rate.date) }) {
                    getExchangeRateFromApi(date)?.let { exchangeRateDao.addExchangeRate(it) }
                }
            }
            exchangeRateDao.getExchangeRatesForLastWeek(dates[0])
        }
    }

    fun getExchangeRatesForLastMonth(lastMonth: Date): Flow<List<ExchangeRateEntity>> {
        return exchangeRateDao.getExchangeRatesForLastMonth(lastMonth)
    }
}