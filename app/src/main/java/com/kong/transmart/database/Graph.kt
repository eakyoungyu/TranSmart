package com.kong.transmart.database

import android.content.Context
import androidx.room.Room
import com.kong.transmart.repositories.BankRepository
import com.kong.transmart.repositories.ExchangeRateRepository

object Graph {
    lateinit var database: BankDatabase

    val bankRepository by lazy {
        BankRepository(bankDao = database.bankDao())
    }

    val exchangeRateRepository by lazy {
        ExchangeRateRepository(exchangeRateDao = database.exchangeRateDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, BankDatabase::class.java, "bank.db")
            .createFromAsset("default-bank.db")
            .addMigrations(migration_1_2)
            .build()
    }
}