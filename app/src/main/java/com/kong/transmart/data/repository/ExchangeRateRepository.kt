package com.kong.transmart.data.repository

import android.util.Log
import com.kong.transmart.data.csv.CsvParser
import com.kong.transmart.data.local.ExchangeRateDAO
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.data.remote.CurrencyRateScraper
import com.kong.transmart.data.remote.ParsedCurrentCurrencyRate
import com.kong.transmart.util.DateUtils
import com.kong.transmart.util.StringUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

class ExchangeRateRepository @Inject constructor(
    private val exchangeRateDao: ExchangeRateDAO,
    private val csvParser: CsvParser,
    private val currencyRateScraper: CurrencyRateScraper
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
                    val curRate = StringUtils.numberFormatToDouble(currentDate.second)
                    acc.add(ExchangeRateEntity(rate = curRate, date = curDate))
                    acc
                }

                filledRates.forEach {
                    exchangeRateDao.addExchangeRate(it)
                }
            }
        }
    }

    suspend fun fetchCurrentExchangeRate(): ParsedCurrentCurrencyRate {
        val currentRate = currencyRateScraper.fetchCurrencyRate()

        val exchangeRate = ExchangeRateEntity(
            rate = currentRate.currencyRate,
            date = DateUtils.getToday()
        )

        Log.d(TAG, "fetchCurrentExchangeRate: ${exchangeRate.date}, ${exchangeRate.rate}")

        val searchedExchangeRate = getExchangeRateByDate(exchangeRate.date).first()

        if (searchedExchangeRate == null) {
            addExchangeRate(exchangeRate)
        } else {
            updateExchangeRate(searchedExchangeRate.copy(rate = exchangeRate.rate))
        }

        return currentRate
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
        return exchangeRateDao.getExchangeRateByDate(date)
    }

    suspend fun getExchangeRatesForLastWeek(): Flow<List<ExchangeRateEntity>>  {
        val dates = DateUtils.getLastWeekDatesToToday()
        return exchangeRateDao.getExchangeRatesForLastWeek(dates.first())
    }

    suspend fun getExchangeRatesForLastMonth(): Flow<List<ExchangeRateEntity>> {
        val dates = DateUtils.getLastMonthDatesToToday()
        return exchangeRateDao.getExchangeRatesForLastMonth(dates.first())
    }

    suspend fun getExchangeRatesForLastYear(): Flow<List<ExchangeRateEntity>> {
        val dates = DateUtils.getLastYearDatesToToday()
        return exchangeRateDao.getExchangeRatesForLastYear(dates.first())
    }
}