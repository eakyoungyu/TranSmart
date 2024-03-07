package com.kong.transmart.viewmodels

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kong.transmart.BuildConfig
import com.kong.transmart.network.currencyRateApi
import kotlinx.coroutines.launch

class ChartViewModel: ViewModel() {
    private val TAG = ChartViewModel::class.simpleName
    fun fetchExchangeRate() {
        fetchExchangeRateByDate("20240116")
    }

    private fun fetchExchangeRateByDate(searchDate: String) {
        viewModelScope.launch {
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
    }
}
