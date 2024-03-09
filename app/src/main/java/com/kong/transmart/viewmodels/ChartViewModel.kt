package com.kong.transmart.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kong.transmart.database.Graph
import com.kong.transmart.models.ExchangeRateEntity
import com.kong.transmart.repositories.ExchangeRateRepository
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

//            exchangeRateRepository.addExchangeRate(ExchangeRateEntity(rate = 988.3, date = date))
            fetchExchangeRate()
        }
    }

    fun fetchExchangeRate() {
        fetchExchangeRateByDate("20240116")
    }

    private fun fetchExchangeRateByDate(searchDate: String) {
        viewModelScope.launch {
            exchangeRateRepository.test(searchDate)

        }
    }

}
