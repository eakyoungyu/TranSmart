package com.kong.transmart.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kong.transmart.R
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.data.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository
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


    private fun fetchExchangeRateByDate(searchDate: String) {
        viewModelScope.launch {
            exchangeRateRepository.getExchangeRatesForLastWeek()

        }
    }

    fun onPeriodSelected(period: Period) {
        _selectedPeriod.value = period
    }

    fun getSelectedPeriod(): Period {
        return _selectedPeriod.value
    }


}

enum class Period(@StringRes val stringResourceId: Int) {
    Week(R.string.period_week),
    Month(R.string.period_month),
    Year(R.string.period_year)
}