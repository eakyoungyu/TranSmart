package com.kong.transmart.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kong.transmart.R
import com.kong.transmart.model.Bank
import com.kong.transmart.viewmodel.CurrencyRateViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BankListView(viewModel: CurrencyRateViewModel) {
        val banks = viewModel.getAllBanks.collectAsState(initial = listOf())
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
                    BankRowView(
                        name = stringResource(id = R.string.label_bank_name),
                        rate = stringResource(id = R.string.label_bank_rate),
                        fee = stringResource(id = R.string.label_bank_fee),
                        total = stringResource(id = R.string.label_bank_total)
                    )
                }

                items(banks.value, key={bank-> bank.id}) {
                        bank ->
                    Spacer(modifier = Modifier.padding(8.dp))
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (bank.removable && it == DismissValue.DismissedToStart) {
                                viewModel.deleteBank(bank)
                            }
                            bank.removable
                        }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        background = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                            )
                        },
                        directions = setOf(DismissDirection.EndToStart),
                        dismissThresholds = {FractionalThreshold(0.5f)},
                        dismissContent = {
                            BankItemView(viewModel, bank = bank)
                        }
                    )
                }
            }
            if (viewModel.isAddButtonClicked()) {
                AddBankItemView(viewModel)
            } else {
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onClick = {
                        viewModel.onAddButtonClicked()
                        viewModel.clearBankInfo()
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add a bank")
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
        BankInfoText(text = name, modifier = Modifier.weight(1.5f))
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
                Text(hint, style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondary), textAlign = TextAlign.Center)
            innerTextField()
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
    )
}

@Composable
fun AddBankItemView(viewModel: CurrencyRateViewModel) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        ) {
        BankInputBasicTextField(viewModel.bankName, Modifier.weight(1.5f), KeyboardType.Text, "name")
        BankInputBasicTextField(viewModel.bankRate, Modifier.weight(1f), KeyboardType.Number, "rate")
        BankInputBasicTextField(viewModel.bankFee, Modifier.weight(1f), KeyboardType.Number, "fee")
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Save bank",
            modifier = Modifier
                .weight(1.5f)
                .clickable {
                    viewModel.onAddSaveButtonClicked()
                    if (viewModel.bankName.value.isNotEmpty() && viewModel.bankRate.value.isNotEmpty()) {
                        viewModel.addBank(
                            Bank(
                                name = viewModel.bankName.value,
                                fee = viewModel.bankFee.value.toDoubleOrNull() ?: 0.0,
                                exchangeRate = viewModel.bankRate.value.toDoubleOrNull() ?: 0.0
                            )
                        )
                    }
                    viewModel.clearBankInfo()
                })

    }
}

@Composable
fun EditBankItemView(viewModel: CurrencyRateViewModel, bank: Bank) {
    viewModel.bankName.value = bank.name
    viewModel.bankFee.value = bank.fee.toString()
    viewModel.bankRate.value = bank.exchangeRate.toString()

    Row (
        modifier = Modifier.fillMaxWidth(),

        ) {
        BankInputBasicTextField(viewModel.bankName, Modifier.weight(1.5f), KeyboardType.Text, "name")
        BankInputBasicTextField(viewModel.bankRate, Modifier.weight(1f), KeyboardType.Number, "rate")
        BankInputBasicTextField(viewModel.bankFee, Modifier.weight(1f), KeyboardType.Number, "fee")
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Save bank",
            modifier = Modifier
                .weight(1.5f)
                .clickable {
                    viewModel.onEditSaveButtonClicked()
                    viewModel.updateBank(
                        Bank(
                            id = bank.id,
                            name = viewModel.bankName.value,
                            fee = viewModel.bankFee.value.toDoubleOrNull() ?: 0.0,
                            exchangeRate = viewModel.bankRate.value.toDoubleOrNull() ?: 0.0
                        )
                    )
                    viewModel.clearBankInfo()
                })
    }
}

@Composable
fun BankItemView(viewModel: CurrencyRateViewModel, bank: Bank) {
    val total = (bank.exchangeRate * viewModel.getSourceAmount()) + bank.fee

    Box(modifier = Modifier.clickable {
        if (bank.removable)
            viewModel.onEditButtonClicked(bank.id)
    }) {
        if (viewModel.isEditButtonClicked(bank.id)) {
            EditBankItemView(viewModel, bank)
        } else {
            BankRowView(
                name = bank.name,
                rate = String.format("%,.2f", bank.exchangeRate),
                fee = bank.fee.toString(),
                total = String.format("%,.2f", total)
            )
        }
    }
}
