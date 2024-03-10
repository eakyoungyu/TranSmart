package com.kong.transmart.model
sealed class Currency(val code: String, val name: String, val symbol: String) {
    data object CanadaCurrency: Currency("CAD", "Canada", "$")
    data object KoreaCurrency: Currency("KRW", "Korea", "\u20A9")
}