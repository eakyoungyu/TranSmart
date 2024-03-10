package com.kong.transmart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kong.transmart.model.Bank
import com.kong.transmart.model.ExchangeRateEntity


@Database(
    entities = [Bank::class, ExchangeRateEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class BankDatabase:RoomDatabase() {
    abstract fun bankDao(): BankDAO
    abstract fun exchangeRateDao(): ExchangeRateDAO
}

val migration_1_2 = object: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `exchange-rate-table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exchange-rate-rate` REAL NOT NULL, `exchange-rate-date` INTEGER NOT NULL)")
    }
}
