package com.kong.transmart.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

@Composable
fun MainView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CurrencyRateView()
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        CurrencyCalculateView()
    }
}

@Composable
fun CurrencyRateView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "1 CAD = 998.89 KRW",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun CurrencyCalculateView() {
    var targetAmount by remember {
        mutableStateOf("100")
    }
    var bestDstAmount by remember {
        mutableStateOf("100000")
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
                Text(text = "CAD", style = TextStyle(fontSize = 20.sp))

                BasicTextField(
                    value = TextFieldValue(text = "$ $targetAmount", selection = TextRange("$ $targetAmount".length)),
                    onValueChange = {
                        val newTarget = it.text.drop(2)
                        if (newTarget.isDigitsOnly())
                            targetAmount = newTarget
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.End),
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
                Text(text = "KRW", style = TextStyle(fontSize = 20.sp))
                Text(
                    text = "\u20A9 " + String.format("%,d", bestDstAmount.toInt()),
                    style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.End),
                )
            }
        }
    }

}

@Composable
fun CurrencyRateChartView() {
    
}

@Composable
fun BankListView() {
    
}


@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView()
}
