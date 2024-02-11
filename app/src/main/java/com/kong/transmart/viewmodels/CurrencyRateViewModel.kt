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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CurrencyRateViewModel(
    private val bankRepository: BankRepository = Graph.bankRepository
): ViewModel() {
    private val TAG = CurrencyRateViewModel::class.simpleName
    private val KAKAO_BANK = "Kakao Bank"

    private val _exchangeRate: MutableState<ExchangeRate> = mutableStateOf<ExchangeRate>(
        ExchangeRate(
        Currency.CanadaCurrency,
        Currency.KoreaCurrency,
        0.0,
            ""
    ))

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
            val scraper = async {
                fetchFromWeb()
            }
            val banks = async {
                bankRepository.getBanks()
            }

            try {
                getAllBanks = banks.await()
                scraper.await()
            } catch (exception: Exception) {
                Log.e(TAG, exception.message.toString())
            }

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
        return _exchangeRate.value.updatedTime
    }

    suspend fun fetchFromWeb() {
        Log.d(TAG, "fetchFromWeb - E")
        val scraper = CurrencyRateScraper()
        val currentCurrencyRate = scraper.fetchCurrencyRate()
        _exchangeRate.value = _exchangeRate.value.copy(
            rate = currentCurrencyRate.currencyRate,
            updatedTime = currentCurrencyRate.time
        )

        updateBankRateByName(KAKAO_BANK, currentCurrencyRate.preferentialExchangeRate)
    }
}