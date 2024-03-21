package com.kong.transmart.data.repository

import com.kong.transmart.data.local.BankDAO
import com.kong.transmart.model.Bank
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BankRepository @Inject constructor (private val bankDao: BankDAO){
    suspend fun addBank(bank: Bank) {
        bankDao.addBank(bank)
    }

    fun getBanks(): Flow<List<Bank>> = bankDao.getAllBanks()

    fun getBankById(id: Long): Flow<Bank> {
        return bankDao.getBankById(id)
    }

    suspend fun updateBankRateByName(name: String, rate: Double) {
        bankDao.updateBankRateByName(name, rate)
    }

    suspend fun updateBank(bank: Bank) {
        bankDao.updateBank(bank)
    }

    suspend fun deleteBank(bank: Bank) {
        bankDao.deleteBank(bank)
    }
}