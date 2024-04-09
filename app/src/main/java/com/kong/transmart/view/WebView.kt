package com.kong.transmart.view

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.kong.transmart.viewmodel.WebViewModel

sealed class WebScreen(val name: String, val url: String) {
    // TODO Move names to strings.xml
    object Naver: WebScreen(
        name = "Naver",
        url = "https://m.stock.naver.com/marketindex/exchange/FX_CADKRW"
    )
    object Moin: WebScreen(
        name = "Moin",
        url = "https://www.themoin.com/currency/info/cad"
    )
    object Wirebarley: WebScreen(
        name = "Wirebarley",
        url = "https://www.wirebarley.com/"
    )
}

@Composable
fun WebView() {
    val webViewModel = hiltViewModel<WebViewModel>()

    val webScreens = listOf(
        WebScreen.Naver,
        WebScreen.Moin,
        WebScreen.Wirebarley
    )

    Column {
        Row (
            modifier = Modifier.fillMaxWidth(),
        ) {
            webScreens.forEachIndexed {
                index, webScreen ->
                val backgroundColor = if (webViewModel.getSelectedTab() == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                val textColor = if (webViewModel.getSelectedTab() == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

                Text(
                    text = webScreen.name,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { webViewModel.setSelectedTab(index) }
                        .background(backgroundColor)
                        .padding(8.dp)
                )
            }
        }
        WebViewScreen(url = webScreens[webViewModel.getSelectedTab()].url)
    }
}

@Composable
fun WebViewScreen(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                settings.domStorageEnabled = true
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        },
    )
}
