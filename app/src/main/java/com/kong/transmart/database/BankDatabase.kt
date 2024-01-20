package com.kong.transmart.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kong.transmart.models.Bank


@Database(
    entities = [Bank::class],
    version = 1,
    exportSchema = false
)
abstract class BankDatabase:RoomDatabase() {
    abstract fun bankDao(): BankDAO
}