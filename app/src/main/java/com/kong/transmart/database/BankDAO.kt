package com.kong.transmart.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kong.transmart.models.Bank
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BankDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addBank(bankEntity: Bank)

    @Query("Select * from `bank-table`")
    abstract fun getAllBanks(): Flow<List<Bank>>

    @Query("Select * from `bank-table` where id=:id")
    abstract fun getBankById(id: Long): Flow<Bank>

    @Update
    abstract suspend fun updateBank(bankEntity: Bank)

    @Delete
    abstract suspend fun deleteBank(bankEntity: Bank)
}