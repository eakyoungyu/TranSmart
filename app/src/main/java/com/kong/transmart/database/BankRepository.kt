package com.kong.transmart.database

import com.kong.transmart.models.Bank
import kotlinx.coroutines.flow.Flow

class BankRepository(private val bankDao: BankDAO){
    suspend fun addBank(bank: Bank) {
        bankDao.addBank(bank)
    }

    fun getBanks(): Flow<List<Bank>> = bankDao.getAllBanks()

    fun getBankById(id: Long): Flow<Bank> {
        return bankDao.getBankById(id)
    }

    suspend fun updateBank(bank: Bank) {
        bankDao.updateBank(bank)
    }

    suspend fun deleteBank(bank: Bank) {
        bankDao.deleteBank(bank)
    }
}