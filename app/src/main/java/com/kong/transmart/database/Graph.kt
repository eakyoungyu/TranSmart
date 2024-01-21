package com.kong.transmart.database

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: BankDatabase

    val bankRepository by lazy {
        BankRepository(bankDao = database.bankDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, BankDatabase::class.java, "bank.db")
            .createFromAsset("default-bank.db").build()
    }
}