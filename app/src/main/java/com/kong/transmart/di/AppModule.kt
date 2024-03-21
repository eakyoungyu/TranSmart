package com.kong.transmart.di

import android.content.Context
import androidx.room.Room
import com.kong.transmart.data.csv.CsvParser
import com.kong.transmart.data.local.BankDAO
import com.kong.transmart.data.local.BankDatabase
import com.kong.transmart.data.local.ExchangeRateDAO
import com.kong.transmart.data.local.migration_1_2
import com.kong.transmart.data.remote.CurrencyRateScraper
import com.kong.transmart.data.repository.BankRepository
import com.kong.transmart.data.repository.ExchangeRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BankDatabase {
        return Room.databaseBuilder(context, BankDatabase::class.java, "bank.db")
            .createFromAsset("default-bank.db")
            .addMigrations(migration_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrencyRateScraper(): CurrencyRateScraper {
        return CurrencyRateScraper()
    }

    @Singleton
    @Provides
    fun provideCsvParser(@ApplicationContext context: Context): CsvParser {
        return CsvParser(context.assets.open("exchange_rate_cad.csv"))
    }

    @Singleton
    @Provides
    fun provideBankDAO(database: BankDatabase): BankDAO {
        return database.bankDao()
    }

    @Singleton
    @Provides
    fun provideExchangeRateDAO(database: BankDatabase): ExchangeRateDAO {
        return database.exchangeRateDao()
    }

    @Singleton
    @Provides
    fun provideBankRepository(bankDao: BankDAO): BankRepository {
        return BankRepository(bankDao)
    }

    @Singleton
    @Provides
    fun provideExchangeRateRepository(exchangeRateDao: ExchangeRateDAO, csvParser: CsvParser, currencyRateScraper: CurrencyRateScraper): ExchangeRateRepository {
        return ExchangeRateRepository(exchangeRateDao, csvParser, currencyRateScraper)
    }

//    private fun createCurrencyRateApi(): CurrencyRateApi {
//        val retrofit = Retrofit.Builder().baseUrl("https://www.koreaexim.go.kr/site/program/financial/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        return retrofit.create(CurrencyRateApi::class.java)
//    }
}