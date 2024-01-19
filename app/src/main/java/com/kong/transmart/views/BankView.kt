package com.kong.transmart.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kong.transmart.models.Bank
import com.kong.transmart.viewmodels.CurrencyRateViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BankListView(viewModel: CurrencyRateViewModel) {
    var isAddButtonClicked = remember {
        mutableStateOf(false)
    }

    Card (
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 8.dp
    ) {
        val banks = viewModel.getBankList()
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                stickyHeader {
                    BankRowView(name = "Name", rate = "Rate", fee = "Fee", total = "Total")
                }

                items(banks) {
                        bank ->
                    Divider(modifier = Modifier.padding(8.dp))
                    BankItemView(bank = bank, viewModel.getSourceAmount())
                }
            }
            if (isAddButtonClicked.value) {
                AddBankItemView(isAddButtonClicked)
            } else {
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onClick = { isAddButtonClicked.value = true }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add a bank")
                }
            }

        }

    }

}

@Composable
fun BankInfoText(text: String, modifier: Modifier) {
    Text(text = text, modifier = modifier, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun BankRowView(name: String, rate: String, fee: String, total: String) {
    Row (
        modifier = Modifier.fillMaxWidth(),
    ) {
        BankInfoText(text = name, modifier = Modifier.weight(1f))
        BankInfoText(text = rate, modifier = Modifier.weight(1f))
        BankInfoText(text = fee, modifier = Modifier.weight(1f))
        BankInfoText(text = total, modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun BankInputBasicTextField(textState: MutableState<String>, modifier: Modifier, keyboardType: KeyboardType, hint: String) {
    BasicTextField(
        value = textState.value,
        onValueChange = {textState.value = it},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
        decorationBox = {innerTextField ->
            if (textState.value.isEmpty())
                Text(hint, color = Color.Gray, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            innerTextField()
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
    )
}

@Composable
fun AddBankItemView(isAddButtonClicked: MutableState<Boolean>) {
    val name = remember {
        mutableStateOf("")
    }
    val rate = remember {
        mutableStateOf("")
    }
    val fee = remember {
        mutableStateOf("")
    }
    Row (
        modifier = Modifier.fillMaxWidth(),

        ) {
        BankInputBasicTextField(name, Modifier.weight(1f), KeyboardType.Text, "name")
        BankInputBasicTextField(rate, Modifier.weight(1f), KeyboardType.Number, "rate")
        BankInputBasicTextField(fee, Modifier.weight(1f), KeyboardType.Number, "fee")
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Save bank",
            modifier = Modifier.weight(1.5f).clickable {
                isAddButtonClicked.value = false
            })
    }
}

@Composable
fun BankItemView(bank: Bank, sourceAmount: Int) {
    val total = (bank.exchangeRate * sourceAmount) + bank.fee
    BankRowView(
        name = bank.name,
        rate = String.format("%,.2f", bank.exchangeRate),
        fee = bank.fee.toString(),
        total = String.format("%,.2f", total)
    )
}
