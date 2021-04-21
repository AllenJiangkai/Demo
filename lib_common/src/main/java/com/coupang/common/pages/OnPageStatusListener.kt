package com.coupang.common.pages

import android.view.View
import com.coupang.common.pages.PageStatusManager

abstract class OnPageStatusListener {

    abstract fun setRetryEvent(retryView: View?)

    fun setLoadingEvent(loadingView: View?) {}

    fun setEmptyEvent(emptyView: View?) {}
}