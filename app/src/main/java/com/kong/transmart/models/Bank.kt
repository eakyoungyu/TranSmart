package com.kong.transmart.models

data class Bank (
    val name: String,
    var fee: Double,
    var exchangeRate: ExchangeRate,
)

val testExchangeRate = ExchangeRate(Currency.CanadaCurrency, Currency.KoreaCurrency, 988.64)
val testBanks = listOf<Bank>(
    Bank("Kakao", 5000.0, testExchangeRate),
    Bank("KB", 2000.0, testExchangeRate),
    Bank("Moin", 0.0, testExchangeRate)
)