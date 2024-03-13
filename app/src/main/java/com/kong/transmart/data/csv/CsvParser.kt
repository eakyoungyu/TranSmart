package com.kong.transmart.data.csv

import android.content.Context
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader

class CsvParser(private val inputStream: InputStream) {
    suspend fun parseCsv(): Map<String, String> {
        val csvReader = CSVReader(InputStreamReader(inputStream))
        val lines = csvReader.readAll()

        val dates = lines.first().drop(1)
        val rates = lines.last().drop(1)

        val exchangeRateMap = dates.zip(rates).toMap()
        csvReader.close()

        return exchangeRateMap
    }
}