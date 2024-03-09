package com.kong.transmart.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import com.kong.transmart.models.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
abstract class ExchangeRateDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addExchangeRate(exchangeRate: ExchangeRateEntity)

    @Update
    abstract suspend fun updateExchangeRate(exchangeRate: ExchangeRateEntity)

    @Delete
    abstract suspend fun deleteExchangeRate(exchangeRate: ExchangeRateEntity)

    @Query("DELETE FROM `exchange-rate-table`")
    abstract suspend fun deleteAllExchangeRates()

    @Query("SELECT * FROM `exchange-rate-table`")
    abstract fun getAllExchangeRates(): Flow<List<ExchangeRateEntity>>

    @Query("SELECT `exchange-rate-date` FROM `exchange-rate-table` ORDER BY `exchange-rate-date` DESC LIMIT 1")
    abstract fun getMostRecentDate(): Flow<Date>

    @Query("SELECT * FROM `exchange-rate-table` WHERE `exchange-rate-date` =:date")
    abstract fun getExchangeRateByDate(date: Date): Flow<ExchangeRateEntity>

    @Query("SELECT * FROM `exchange-rate-table` WHERE `exchange-rate-date` >= :lastWeek ORDER BY `exchange-rate-date` ASC")
    abstract fun getExchangeRatesForLastWeek(lastWeek: Date): Flow<List<ExchangeRateEntity>>

    @Query("SELECT * FROM `exchange-rate-table` WHERE `exchange-rate-date` >= :lastMonth ORDER BY `exchange-rate-date` ASC")
    abstract fun getExchangeRatesForLastMonth(lastMonth: Date): Flow<List<ExchangeRateEntity>>
}

class DateConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}