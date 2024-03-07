package com.kong.transmart.network

import com.kong.transmart.models.ExchangeRateResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("https://www.koreaexim.go.kr/site/program/financial/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val currencyRateApi = retrofit.create(CurrencyRateApi::class.java)

interface CurrencyRateApi {
    @GET("exchangeJSON")
    suspend fun getExchangeRate(
        @Query("authkey") authkey: String,
        @Query("searchdate") searchdate: String,
        @Query("data") data: String
    ): List<ExchangeRateResponse>?
}