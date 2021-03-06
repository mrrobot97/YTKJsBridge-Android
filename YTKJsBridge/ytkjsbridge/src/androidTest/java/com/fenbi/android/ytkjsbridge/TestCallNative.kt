package com.fenbi.android.ytkjsbridge

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by yangjw on 2019/1/9.
 */
@RunWith(AndroidJUnit4::class)
class TestCallNative {

    private fun initWebView() = WebView(InstrumentationRegistry.getTargetContext()).apply {
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()
        initYTKJsBridge()
    }

    @Test
    fun testCallNative() {
        val countDownLatch = CountDownLatch(1)
        var ret: String? = ""
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val webView = initWebView()
            webView.addYTKJavascriptInterface(object {
                @JavascriptInterface
                fun testNative(msg: String?) {
                    ret = msg
                    countDownLatch.countDown()
                }
            })
            webView.loadUrl("file:///android_asset/test-call-native.html")
        }
        countDownLatch.await(60, TimeUnit.SECONDS)
        assertEquals("hello world", ret)
    }

}