package com.kong.transmart.view

import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WebView() {
    WebViewScreen()
}

@Composable
fun WebViewScreen() {

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(10000) // 10 seconds

                            view?.evaluateJavascript(
                                "document.documentElement.outerHTML"
                            ) { html ->
                                Log.d("WebViewHTML", html ?: "HTML content not found")
                            }
                        }
                    }
                }

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                settings.domStorageEnabled = true
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        },
        update = { webView ->
            // https://www.themoin.com/currency/info/cad
            // client side exception - https://www.wirebarley.com/
            webView.loadUrl("https://www.wirebarley.com/")

        },


        )
}