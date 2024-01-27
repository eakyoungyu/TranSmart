package com.kong.transmart.models

data class ExchangeRate (
    val from: Currency,
    val to: Currency,
    var rate: Double,
    var updatedTime: String
)