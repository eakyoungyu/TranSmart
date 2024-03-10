package com.kong.transmart.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kong.transmart.data.local.Graph
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.data.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChartViewModel(
    private val exchangeRateRepository: ExchangeRateRepository = Graph.exchangeRateRepository
): ViewModel() {
    private val TAG = ChartViewModel::class.simpleName

    lateinit var getAllExchangeRate: Flow<List<ExchangeRateEntity>>

    init {
//        val dateFormat = SimpleDateFormat("yyyyMMdd")
//        val date = dateFormat.parse("20240116")

        viewModelScope.launch {
            getAllExchangeRate = exchangeRateRepository.getAllExchangeRates()
            exchangeRateRepository.deleteAllExchangeRates()
            fetchExchangeRate()
        }
    }

    fun fetchExchangeRate() {
        fetchExchangeRateByDate("20240116")
    }

    private fun fetchExchangeRateByDate(searchDate: String) {
        viewModelScope.launch {
            Log.d(TAG, "fetchExchangeRateByDate: $searchDate")
            exchangeRateRepository.getExchangeRatesForLastWeek()

        }
    }

}
