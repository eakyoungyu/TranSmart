package com.kong.transmart.data.remote

import android.util.Log
import com.kong.transmart.util.StringUtils
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.table
import it.skrape.selects.html5.tbody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.round

data class CurrentCurrencyRate(
    var currencyRate: String,
    var transferRate: String
)

data class ParsedCurrentCurrencyRate(
    val currencyRate: Double,
    val preferentialExchangeRate: Double,
    val time: String
)
class CurrencyRateScraper {
    private val TAG = CurrencyRateScraper::class.simpleName
    private val naverFinanceURL = "https://finance.naver.com/marketindex/exchangeDetail.naver?marketindexCd=FX_CADKRW"
    private val moinUrl = "https://www.themoin.com/currency/info/cad"
    private val wirebarleyUrl = "https://www.wirebarley.com/"
    suspend fun fetchCurrencyRate(): ParsedCurrentCurrencyRate {
        Log.i(TAG, "Start fetching")

        try {
            val currentCurrencyRate = retry(3) {
                fetchCurrentCurrencyRate(naverFinanceURL)
            }
            return parseCurrencyRate(currentCurrencyRate)
        } catch (exception: Exception) {
            Log.e(TAG, "Network error: Failed to fetchCurrencyRate() ${exception.message}")
        }

        return parseCurrencyRate(CurrentCurrencyRate("0.0", "0.0"))
    }

    private suspend fun <T> retry(numberOfRetries: Int, block: suspend () -> T): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (exception: Exception) {
                Log.e(TAG, "Network error in retry: Failed to fetchCurrencyRate() ${exception.message}")
            }
        }

        return block()
    }

    private fun getCurrentTime(): String {
        val currentDate = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss")

        return simpleDateFormat.format(currentDate)
    }

    private fun parseCurrencyRate(rate: CurrentCurrencyRate): ParsedCurrentCurrencyRate {
        val currencyRate = StringUtils.numberFormatToDouble(rate.currencyRate)
        val transferRate = StringUtils.numberFormatToDouble(rate.transferRate)

        val exchangeFee = (transferRate - currencyRate) * 0.5
        var exchangeRate = currencyRate + exchangeFee
        exchangeRate = round(exchangeRate * 100) / 100

        Log.d(TAG, "Transfer: $transferRate Calculated: $exchangeRate")

        return ParsedCurrentCurrencyRate(currencyRate, exchangeRate, getCurrentTime())
    }

    private suspend fun fetchCurrentCurrencyRate(targetUrl: String): CurrentCurrencyRate =
        withContext(Dispatchers.IO) {
            val currencyRate: CurrentCurrencyRate = CurrentCurrencyRate("0.0", "0.0")
            Log.d(TAG, "fetchCurrentCurrencyRate - E")

            skrape(HttpFetcher) {
                request {
                    url = targetUrl
                    timeout = 10000
                }

                response {
                    Log.d(TAG, "${responseStatus.code} ${responseStatus.message}")
                    if (responseStatus.code != 200) {
                        throw Exception("Response code: ${responseStatus.code} - ${responseStatus.message}")
                    }
                    htmlDocument {
                        table {
                            withClass = "tbl_calculator"
                            tbody {
                                findFirst {
                                    val exchangeRate = this.findFirst("td").text
                                    Log.d(TAG, "Exchange Rate: $exchangeRate")
                                    currencyRate.currencyRate = exchangeRate
                                }
                            }
                        }
                        table {
                            withClass = "tbl_exchange"

                            tbody {
                                val sendingRow = findAll("tr").find {
                                    it.findFirst("th").text == "송금 보내실때"
                                }

                                val rate = sendingRow?.findFirst("td")?.text
                                if (rate != null) {
                                    currencyRate.transferRate = rate
                                }
                            }
                        }
                    }
                }
            }

            return@withContext currencyRate
    }

}