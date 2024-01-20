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
//    private val _bankList = mutableStateOf(testBanks)
//    private val _bankList = mutableStateOf(mutableListOf(Bank(0, KAKAO_BANK, 8000.0, 0.0)))

    private val _sourceAmount = mutableStateOf(0)
    val bankName = mutableStateOf("")
    val bankFee = mutableStateOf("")
    val bankRate = mutableStateOf("")

    private val _isAddButtonClicked = mutableStateOf(false)
    private val _isEditButtonClicked = mutableStateOf(false)
    val selectedBankId = mutableStateOf(-1L)

    lateinit var getAllBanks: Flow<List<Bank>>
    init {
//        fetchFromWeb()
        viewModelScope.launch(Dispatchers.IO) {
            getAllBanks = bankRepository.getBanks()
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
        selectedBankId.value = -1
    }

    fun getSourceAmount(): Int {
        return _sourceAmount.value
    }

    fun setSourceAmount(amount: Int) {
        _sourceAmount.value = amount
    }


//    fun getBankList(): List<Bank> {
//        return _bankList.value
//    }

    fun getSourceCurrency(): Currency {
        return _exchangeRate.value.from
    }

    fun getTargetCurrency(): Currency {
        return _exchangeRate.value.to
    }

    fun getCurrencyRate(): Double {
        return _exchangeRate.value.rate
    }

//    private fun fetchFromWeb() {
//        viewModelScope.launch {
//            val scraper = CurrencyRateScraper()
//            val currentCurrencyRate = scraper.fetchCurrencyRate()
//            _exchangeRate.value = _exchangeRate.value.copy(rate = currentCurrencyRate.currencyRate)
//            _bankList.value.forEach {
//                bank ->
//                if (bank.name == KAKAO_BANK) {
//                    _bankList.value = _bankList.value.toMutableList().apply {
//                        val exchangeFee = (currentCurrencyRate.transferRate - currentCurrencyRate.currencyRate) * 0.5
//                        val exchangeRate = currentCurrencyRate.currencyRate + exchangeFee
//                        set(indexOf(bank), bank.copy(exchangeRate = round(exchangeRate * 100) / 100))
//                        Log.d("Y2K2", "Transfer: ${currentCurrencyRate.transferRate} Calculated: ${currentCurrencyRate.currencyRate + exchangeFee}")
//                    }
//                }
//            }
//        }
//    }
}