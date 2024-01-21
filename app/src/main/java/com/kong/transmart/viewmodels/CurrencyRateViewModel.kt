package com.kong.transmart.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kong.transmart.database.BankRepository
import com.kong.transmart.database.Graph
import com.kong.transmart.models.Bank
import com.kong.transmart.models.Currency
import com.kong.transmart.models.ExchangeRate
import com.kong.transmart.network.CurrencyRateScraper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.round

class CurrencyRateViewModel(
    private val bankRepository: BankRepository = Graph.bankRepository
): ViewModel() {
    private val KAKAO_BANK = "Kakao Bank"

    private val _exchangeRate: MutableState<ExchangeRate> = mutableStateOf<ExchangeRate>(
        ExchangeRate(
        Currency.CanadaCurrency,
        Currency.KoreaCurrency,
        0.0
    ))
    private val _updatedTime: MutableState<String> = mutableStateOf("")

    private val _sourceAmount = mutableStateOf(0)
    val bankName = mutableStateOf("")
    val bankFee = mutableStateOf("")
    val bankRate = mutableStateOf("")

    private val _isAddButtonClicked = mutableStateOf(false)
    private val _isEditButtonClicked = mutableStateOf(false)
    private val selectedBankId = mutableStateOf(-1L)

    lateinit var getAllBanks: Flow<List<Bank>>
    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllBanks = bankRepository.getBanks()
        }
        viewModelScope.launch {
            fetchFromWeb()
        }
    }

    fun addBank(bank: Bank) {
        viewModelScope.launch(Dispatchers.IO) {
            bankRepository.addBank(bank)
        }
    }

    fun updateBank(bank: Bank) {
        viewModelScope.launch(Dispatchers.IO) {
            bankRepository.updateBank(bank)
        }
    }

    fun getBankById(id: Long): Flow<Bank> {
        return bankRepository.getBankById(id)
    }

    fun updateBankRateByName(name: String, rate: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            bankRepository.updateBankRateByName(name, rate)
        }
    }

    fun deleteBank(bank: Bank) {
        viewModelScope.launch(Dispatchers.IO) {
            bankRepository.deleteBank(bank)
        }
    }
    fun onAddButtonClicked() {
        _isAddButtonClicked.value = true
        _isEditButtonClicked.value = false
    }

    fun onAddSaveButtonClicked() {
        _isAddButtonClicked.value = false
    }

    fun isAddButtonClicked(): Boolean {
        return _isAddButtonClicked.value
    }

    fun onEditButtonClicked(id: Long) {
        _isEditButtonClicked.value = true
        _isAddButtonClicked.value = false
        selectedBankId.value = id
    }

    fun onEditSaveButtonClicked() {
        _isEditButtonClicked.value = false
    }

    fun isEditButtonClicked(id: Long): Boolean {
        return _isEditButtonClicked.value && selectedBankId.value == id
    }

    fun clearBankInfo() {
        bankName.value = ""
        bankFee.value = ""
        bankRate.value = ""
    }

    fun getSourceAmount(): Int {
        return _sourceAmount.value
    }

    fun setSourceAmount(amount: Int) {
        _sourceAmount.value = amount
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

    fun getLastUpdatedTime(): String {
        return _updatedTime.value
    }

    private suspend fun fetchFromWeb() {
        val scraper = CurrencyRateScraper()
        val currentCurrencyRate = scraper.fetchCurrencyRate()
        _exchangeRate.value = _exchangeRate.value.copy(rate = currentCurrencyRate.currencyRate)
        _updatedTime.value = currentCurrencyRate.time

        val exchangeFee = (currentCurrencyRate.transferRate - currentCurrencyRate.currencyRate) * 0.5
        var exchangeRate = currentCurrencyRate.currencyRate + exchangeFee
        exchangeRate = round(exchangeRate * 100) / 100

        updateBankRateByName(KAKAO_BANK, exchangeRate)
        Log.d("Y2K2", "Transfer: ${currentCurrencyRate.transferRate} Calculated: ${currentCurrencyRate.currencyRate + exchangeFee}")

    }
}