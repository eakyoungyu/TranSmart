package com.kong.transmart.models

data class Bank (
    val name: String,
    var fee: Double,
    var exchangeRate: ExchangeRate,
)