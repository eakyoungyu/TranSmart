package com.kong.transmart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kong.transmart.R
import com.kong.transmart.viewmodel.ChartViewModel
import com.kong.transmart.viewmodel.CurrencyRateViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView() {
    val viewModel:CurrencyRateViewModel = viewModel()

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        viewModel.fetchFromWeb()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pullRefresh(state)
    ) {
        PullRefreshIndicator(
            refreshing,
            state,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        CurrentCurrencyRateView(viewModel)
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        ChartView(viewModel = ChartViewModel())
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        CurrencyCalculateView(viewModel)
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        BankListView(viewModel)


    }
}

@Composable
fun CurrentCurrencyRateView(viewModel: CurrencyRateViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val sourceCode = viewModel.getSourceCurrency().code
        val targetCode = viewModel.getTargetCurrency().code
        val currencyRate = viewModel.getCurrencyRate()
        Text(
            text = "1 $sourceCode = $currencyRate $targetCode",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "${stringResource(id = R.string.last_updated)}: ${viewModel.getLastUpdatedTime()}",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondary)
        )
    }
}

@Composable
fun CurrencyCalculateView(viewModel: CurrencyRateViewModel) {
    val sourceCurrency = viewModel.getSourceCurrency()
    val targetCurrency = viewModel.getTargetCurrency()
    val currencyRate = viewModel.getCurrencyRate()

    var sourceAmount by remember {
        mutableStateOf("0")
    }
    var bestTargetAmount: Double by remember {
        mutableStateOf(sourceAmount.toDouble() * currencyRate)
    }

    Row (
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = sourceCurrency.code, style = MaterialTheme.typography.bodyLarge)

        BasicTextField(
            value = TextFieldValue(
                text = "${sourceCurrency.symbol} $sourceAmount",
                selection = TextRange("${sourceCurrency.symbol} $sourceAmount".length)
            ),
            onValueChange = {
                val newTarget = it.text.drop(2)
                if (newTarget.isDigitsOnly()) {
                    sourceAmount = newTarget
                    viewModel.setSourceAmount(sourceAmount.toIntOrNull() ?: 0)
                    bestTargetAmount = currencyRate * (sourceAmount.toDoubleOrNull() ?: 0.0)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End, color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.width(IntrinsicSize.Min),
        )
    }

//    Spacer(modifier = Modifier.padding(bottom = 16.dp))
//
//    Row (
//        modifier = Modifier.fillMaxWidth().padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Text(text = targetCurrency.code, style = MaterialTheme.typography.bodyLarge)
//        Text(
//            text = "${targetCurrency.symbol} " + String.format("%,.2f", bestTargetAmount),
//            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
//        )
//    }

}

@Composable
fun CurrencyRateChartView() {
    
}



@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}
