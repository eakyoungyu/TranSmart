package com.kong.transmart.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kong.transmart.models.Bank
import com.kong.transmart.viewmodels.CurrencyRateViewModel

@Composable
fun MainView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val viewModel:CurrencyRateViewModel = viewModel()
        CurrencyRateView(viewModel)
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        CurrencyCalculateView(viewModel)
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        BankListView(viewModel)
    }
}

@Composable
fun CurrencyRateView(viewModel: CurrencyRateViewModel) {
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
    }
}

@Composable
fun CurrencyCalculateView(viewModel: CurrencyRateViewModel) {
    val sourceCurrency = viewModel.getSourceCurrency()
    val targetCurrency = viewModel.getTargetCurrency()
    val currencyRate = viewModel.getCurrencyRate()

    var sourceAmount by remember {
        mutableStateOf("100")
    }
    var bestTargetAmount: Double by remember {
        mutableStateOf(sourceAmount.toDouble() * currencyRate)
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.White,
            elevation = 8.dp
        ) {
            Row (
                modifier = Modifier.padding(16.dp),
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
                            bestTargetAmount = currencyRate * (sourceAmount.toDoubleOrNull() ?: 0.0)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
                    modifier = Modifier.width(IntrinsicSize.Min),
                )
            }
        }

        Spacer(modifier = Modifier.padding(bottom = 16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.White,
            elevation = 8.dp
        ) {
            Row (
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = targetCurrency.code, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "${targetCurrency.symbol} " + String.format("%,.2f", bestTargetAmount),
                    style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
                )
            }
        }
    }

}

@Composable
fun CurrencyRateChartView() {
    
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BankListView(viewModel: CurrencyRateViewModel) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 8.dp
    ) {
        val banks = viewModel.getBankList()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            stickyHeader {
                BankRowView(name = "Name", rate = "Rate", fee = "Fee", total = "Total")
            }

            items(banks) {
                    bank ->
                Divider(modifier = Modifier.padding(8.dp))
                BankItemView(bank = bank)
            }
        }
    }

}

@Composable
fun BankRowView(name: String, rate: String, fee: String, total: String) {
    Row (
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = name, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = rate, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = fee, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = total, modifier = Modifier.weight(1.5f), textAlign = TextAlign.Center)
    }
}

@Composable
fun BankItemView(bank: Bank) {
    BankRowView(name = bank.name, rate = bank.exchangeRate.rate.toString(), fee = bank.fee.toString(), total = "2000,000.00")
}


@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}
