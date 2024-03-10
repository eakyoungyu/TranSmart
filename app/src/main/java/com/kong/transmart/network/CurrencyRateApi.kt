package com.kong.transmart.network

import com.kong.transmart.BuildConfig
import com.kong.transmart.models.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRateApi {
    @GET("exchangeJSON")
    suspend fun getExchangeRate(
        @Query("authkey") authkey: String = BuildConfig.API_KEY,
        @Query("searchdate") searchdate: String,
        @Query("data") data: String = "AP01"
    ): List<ExchangeRateResponse>?
}