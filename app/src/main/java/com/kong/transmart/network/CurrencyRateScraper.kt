package com.kong.transmart.network

import android.util.Log
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Result
import it.skrape.fetcher.extract
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h3
import it.skrape.selects.html5.img
import it.skrape.selects.html5.li
import it.skrape.selects.html5.ol
import it.skrape.selects.html5.table
import it.skrape.selects.html5.tbody
import it.skrape.selects.html5.td
import it.skrape.selects.html5.tr
import it.skrape.selects.text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

data class CurrentCurrencyRate(
    var currencyRate: String,
    var transferRate: String
)

data class ParsedCurrentCurrencyRate(
    val currencyRate: Double,
    val transferRate: Double
)
class CurrencyRateScraper {
    private val naverFinanceURL = "https://finance.naver.com/marketindex/exchangeDetail.naver?marketindexCd=FX_CADKRW"
    private val moinUrl = "https://www.themoin.com/currency/info"
    private val wirebarleyUrl = "https://www.wirebarley.com/"
    suspend fun fetchCurrencyRate(): ParsedCurrentCurrencyRate{
        Log.i("Y2K2", "Start fetching")

        val currentCurrencyRate = fetchCurrentCurrencyRate(naverFinanceURL)
        val result = ParsedCurrentCurrencyRate(stringToDouble(currentCurrencyRate.currencyRate), stringToDouble(currentCurrencyRate.transferRate))
        Log.i("Y2K2", "Result: ${result.currencyRate} ${result.transferRate}")

        return result
    }

    private fun stringToDouble(string: String): Double {
        val numberFormat = NumberFormat.getInstance(Locale.US)
        return numberFormat.parse(string)?.toDouble() ?: 0.0
    }

    private suspend fun fetchCurrentCurrencyRate(targetUrl: String): CurrentCurrencyRate =
        withContext(Dispatchers.IO) {
            val currencyRate: CurrentCurrencyRate = CurrentCurrencyRate("0.0", "0.0")
            skrape(HttpFetcher) {
                request {
                    url = targetUrl
                }
                response {
                    htmlDocument {
                        table {
                            withClass = "tbl_calculator"
                            tbody {
                                findFirst {
                                    val exchangeRate = this.findFirst("td").text
                                    Log.d("Y2K2", "Exchange Rate: $exchangeRate")
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