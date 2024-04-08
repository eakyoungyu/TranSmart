package com.kong.transmart.view

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.kong.transmart.MainActivity
import com.kong.transmart.R
import com.kong.transmart.util.NotificationUtils
import com.kong.transmart.viewmodel.ChartViewModel
import com.kong.transmart.viewmodel.CurrencyRateViewModel

@Composable
fun HomeView() {
    val composeView = LocalView.current
    val currencyRateViewModel = composeView.findViewTreeViewModelStoreOwner()?.let {
        hiltViewModel<CurrencyRateViewModel>(it)
    } ?: run {
        hiltViewModel<CurrencyRateViewModel>()
    }

    val chartViewModel = hiltViewModel<ChartViewModel>()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        PermissionLauncher(context = LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CurrentCurrencyRateView(currencyRateViewModel)
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        ChartView(viewModel = chartViewModel)
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        CurrencyCalculateView(currencyRateViewModel)
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        BankListView(currencyRateViewModel)

    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionLauncher(context: Context) {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
                permissions ->
            if (permissions[Manifest.permission.POST_NOTIFICATIONS] == true) {
                // permission granted
            } else {
                // if permission request is denied before, rationalRequired is true
                val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                )

                if (rationalRequired) {
                    Toast.makeText(context, "Notification permission is required!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Notification permission is required. Turn on it in settings", Toast.LENGTH_LONG).show()
                }
            }
        }
    )
    LaunchedEffect(key1 = true) {
        if (!NotificationUtils.hasPermission(context)) {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            )
        }
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
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onTertiary)
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
        modifier = Modifier
            .fillMaxWidth()
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
