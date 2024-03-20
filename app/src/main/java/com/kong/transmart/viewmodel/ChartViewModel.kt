package com.kong.transmart.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kong.transmart.R
import com.kong.transmart.data.local.Graph
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.data.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChartViewModel(
    private val exchangeRateRepository: ExchangeRateRepository = Graph.exchangeRateRepository
): ViewModel() {
    private val TAG = ChartViewModel::class.simpleName
    private val _selectedPeriod = mutableStateOf(Period.Week)
    lateinit var exchangeRatesWeek: Flow<List<ExchangeRateEntity>>
    lateinit var exchangeRatesMonth: Flow<List<ExchangeRateEntity>>
    lateinit var exchangeRatesYear: Flow<List<ExchangeRateEntity>>
    init {
        viewModelScope.launch {
            exchangeRatesWeek = exchangeRateRepository.getExchangeRatesForLastWeek()
            exchangeRatesMonth = exchangeRateRepository.getExchangeRatesForLastMonth()
            exchangeRatesYear = exchangeRateRepository.getExchangeRatesForLastYear()
//            exchangeRateRepository.deleteAllExchangeRates()
            exchangeRateRepository.loadFromCsv()
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

    fun onPeriodSelected(period: Period) {
        _selectedPeriod.value = period
        Log.d(TAG, "onPeriodSelected: ${period.name}")
    }

    fun getSelectedPeriod(): Period {
        Log.d(TAG, "getSelectedPeriod: ${_selectedPeriod.value.name}")
        return _selectedPeriod.value
    }


}

enum class Period(@StringRes val stringResourceId: Int) {
    Week(R.string.period_week),
    Month(R.string.period_month),
    Year(R.string.period_year)
}