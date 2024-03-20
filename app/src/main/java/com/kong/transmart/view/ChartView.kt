package com.kong.transmart.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hd.charts.LineChartView
import com.hd.charts.common.model.ChartDataSet
import com.hd.charts.style.ChartViewDefaults
import com.hd.charts.style.LineChartDefaults
import com.kong.transmart.model.ExchangeRateEntity
import com.kong.transmart.viewmodel.ChartViewModel
import com.kong.transmart.viewmodel.Period

@Composable
fun ChartView (viewModel: ChartViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)) {
            ChartViewLayer(
                exchangeRates = viewModel.exchangeRatesWeek.collectAsState(initial = listOf()).value,
                visible = viewModel.getSelectedPeriod() == Period.Week
            )

            ChartViewLayer(
                exchangeRates = viewModel.exchangeRatesMonth.collectAsState(initial = listOf()).value,
                visible = viewModel.getSelectedPeriod() == Period.Month
            )

            ChartViewLayer(
                exchangeRates = viewModel.exchangeRatesYear.collectAsState(initial = listOf()).value,
                visible = viewModel.getSelectedPeriod() == Period.Year
            )
        }
    SelectPeriod(viewModel)
    }


}

@Composable
fun SelectPeriod(viewModel: ChartViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        Period.entries.forEach { period ->
            val isSelected = (period == viewModel.getSelectedPeriod())
            val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
            val textColor = if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.onPeriodSelected(period) },
                colors = ButtonDefaults.textButtonColors(backgroundColor = backgroundColor, contentColor = textColor)
            ) {
                Text(
                    text = stringResource(id = period.stringResourceId)
                )
            }
        }
    }
}

@Composable
fun ChartViewLayer(exchangeRates: List<ExchangeRateEntity>, visible: Boolean) {
    if (visible)
        CustomLineChartView(exchangeRates)
}

@Composable
fun CustomLineChartView(exchangeRates: List<ExchangeRateEntity>) {
    val style = LineChartDefaults.style(
        lineColor = MaterialTheme.colorScheme.onPrimary,
//        pointColor = Color.Black,
        pointSize = 0f,
        pointVisible = false,
        bezier = true,
        dragPointColor = MaterialTheme.colorScheme.secondary,
        dragPointVisible = true,
        dragPointSize = 0f,
        dragActivePointSize = 8f,
        chartViewStyle = ChartViewDefaults.style(backgroundColor = MaterialTheme.colorScheme.primary)
//        chartViewStyle =
    )
    if (exchangeRates.isNotEmpty()) {
        val dataSet = ChartDataSet(
            items = exchangeRates.map { it.rate.toFloat() },
            title = ""
        )
        LineChartView(dataSet = dataSet, style = style)
    } else {
        Text("Loading...")
    }
}