package com.kong.transmart.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import com.kong.transmart.util.DateUtils
import com.kong.transmart.viewmodel.ChartViewModel

@Composable
fun ChartView (viewModel: ChartViewModel) {
//    viewModel.fetchExchangeRate()
    // grid to display exchange rate in database
    val exchangeRates = viewModel.getAllExchangeRate.collectAsState(initial = listOf())
    LazyColumn {
        items(exchangeRates.value) { exchangeRate ->
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = DateUtils.dateToString(exchangeRate.date) + " ")
                Text(text = exchangeRate.rate.toString())
            }
        }
    }

}