package com.kong.transmart.models

data class Bank (
    val name: String,
    var fee: Double,
    var exchangeRate: Double,
)

val testBanks = listOf<Bank>(
    Bank("Kakao Bank", 8000.0, 999.1),
    Bank("KB", 2000.0, 999.2),
    Bank("Moin", 5000.0, 999.3)
)