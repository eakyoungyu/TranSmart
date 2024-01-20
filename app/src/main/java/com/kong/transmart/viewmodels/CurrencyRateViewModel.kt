package com.kong.transmart.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kong.transmart.models.Bank
import com.kong.transmart.models.Currency
import com.kong.transmart.models.ExchangeRate
import com.kong.transmart.network.CurrencyRateScraper
import kotlinx.coroutines.launch
import kotlin.math.round

class CurrencyRateViewModel: ViewModel() {
    private val KAKAO_BANK = "Kakao Bank"
    init {
        fetch()
    }
    private val _exchangeRate: MutableState<ExchangeRate> = mutableStateOf<ExchangeRate>(
        ExchangeRate(
        Currency.CanadaCurrency,
        Currency.KoreaCurrency,
        0.0
    ))
//    private val _bankList = mutableStateOf(testBanks)
    private val _bankList = mutableStateOf(mutableListOf(Bank(KAKAO_BANK, 8000.0, 0.0)))

    private val _sourceAmount = mutableStateOf(0)

    fun getSourceAmount(): Int {
        return _sourceAmount.value
    }

    fun setSourceAmount(amount: Int) {
        _sourceAmount.value = amount
    }

    fun getBankList(): List<Bank> {
        return _bankList.value
    }

    fun getSourceCurrency(): Currency {
        return _exchangeRate.value.from
    }

    fun getTargetCurrency(): Currency {
        return _exchangeRate.value.to
    }

    fun getCurrencyRate(): Double {
        return _exchangeRate.value.rate
    }

    private fun fetch() {
        viewModelScope.launch {
            val scraper = CurrencyRateScraper()
            val currentCurrencyRate = scraper.fetchCurrencyRate()
            _exchangeRate.value = _exchangeRate.value.copy(rate = currentCurrencyRate.currencyRate)
//            _bankList.value.find { it.name == KAKAO_BANK }?.copy(exchangeRate = currentCurrencyRate.transferRate)
            _bankList.value.forEach {
                bank ->
                if (bank.name == KAKAO_BANK) {
                    _bankList.value = _bankList.value.toMutableList().apply {
                        val exchangeFee = (currentCurrencyRate.transferRate - currentCurrencyRate.currencyRate) * 0.5
                        val exchangeRate = currentCurrencyRate.currencyRate + exchangeFee
                        set(indexOf(bank), bank.copy(exchangeRate = round(exchangeRate * 100) / 100))
                        Log.d("Y2K2", "Transfer: ${currentCurrencyRate.transferRate} Calculated: ${currentCurrencyRate.currencyRate + exchangeFee}")
                    }
                }
            }
        }
    }
}