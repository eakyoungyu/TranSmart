package com.kong.transmart.util

import java.text.NumberFormat
import java.util.Locale

object StringUtils {
    fun numberFormatToDouble(rate: String): Double {
        val numberFormat = NumberFormat.getInstance(Locale.US)
        return numberFormat.parse(rate)?.toDouble() ?: 0.0
    }
}