package com.coupang.common.widget.webView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * @author Alan_Xiong
 *
 * @desc: x5 webView
 * @time 2020/5/28 1:34 PM
 */
class CommonWebView(p0: Context?, p1: AttributeSet?) : WebView(p0, p1) {

    private var onUrlLoadListener: OnUrlLoadListener? = null

    interface OnUrlLoadListener {
        /**
         * 网络加载异常
         */
        fun onUrlLoadError()
    }

    init {
        initWebViewSettings()
    }

    fun setUrlLoadListener(onUrlLoadListener: OnUrlLoadListener?) {
        this.onUrlLoadListener = onUrlLoadListener
    }

    fun setClient(customClient: WebViewClient?) {
        this.webViewClient = customClient
    }

    private val client: WebViewClient =
        object : WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedError(
                webView: WebView,
                i: Int,
                s: String,
                s1: String
            ) {
                super.onReceivedError(webView, i, s, s1)
                if (onUrlLoadListener != null) {
                    onUrlLoadListener!!.onUrlLoadError()
                }
            }
        }


    private fun initWebViewSettings() {
        val webSetting = this.settings
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        //支持自动加载图片
        webSetting.loadsImagesAutomatically = true
        //设置可以访问文件
        webSetting.allowFileAccess = true
        webSetting.blockNetworkImage = false
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(false)
        webSetting.builtInZoomControls = false
        //将图片调整到适合webview的大小
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(false)
        // 缩放至屏幕的大小
        webSetting.loadWithOverviewMode = true
        webSetting.setAppCacheEnabled(true)
        webSetting.databaseEnabled = true
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(false)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        //设置编码格式
        webSetting.defaultTextEncodingName = "utf-8"
        if (Build.VERSION.SDK_INT >= 21) {
            webSetting.mixedContentMode = WebSettings.LOAD_DEFAULT
        }
    }
}