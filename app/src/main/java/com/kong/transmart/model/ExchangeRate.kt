package com.kong.transmart.model

data class ExchangeRate (
    val from: Currency,
    val to: Currency,
    var rate: Double,
    var updatedTime: String
)