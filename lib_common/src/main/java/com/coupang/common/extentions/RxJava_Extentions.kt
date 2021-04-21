package com.coupang.common.extentions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

fun Disposable.lifeCycle(set: CompositeDisposable) {
    set.add(this)
}