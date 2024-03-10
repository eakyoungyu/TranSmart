package com.kong.transmart.data.local

import android.content.Context
import androidx.room.Room
import com.kong.transmart.data.remote.CurrencyRateApi
import com.kong.transmart.data.repository.BankRepository
import com.kong.transmart.data.repository.ExchangeRateRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Graph {
    lateinit var database: BankDatabase
    lateinit var currencyRateApi: CurrencyRateApi

    val bankRepository by lazy {
        BankRepository(bankDao = database.bankDao())
    }

    val exchangeRateRepository by lazy {
        ExchangeRateRepository(exchangeRateDao = database.exchangeRateDao(), currencyRateApi = currencyRateApi)
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, BankDatabase::class.java, "bank.db")
            .createFromAsset("default-bank.db")
            .addMigrations(migration_1_2)
            .build()
        currencyRateApi = createCurrencyRateApi()
    }

    private fun createCurrencyRateApi(): CurrencyRateApi {
        val retrofit = Retrofit.Builder().baseUrl("https://www.koreaexim.go.kr/site/program/financial/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CurrencyRateApi::class.java)
    }
}