package com.coupang.common.extentions.textview

import android.annotation.SuppressLint
import android.text.method.DigitsKeyListener
import android.text.method.ReplacementTransformationMethod
import android.widget.EditText
import android.widget.TextView
import com.coupang.common.extentions.textview.filter.MoneyInputFilter
import com.coupang.common.extentions.textview.filter.TextLengthLimitInputFilter
import com.coupang.common.extentions.textview.watcher.MoneyFormatTextWatcher
import java.util.*

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

@SuppressLint("SetTextI18n")
fun TextView.idCardText(idCardNum: String) {
    if (idCardNum.length < 2) {
        text = idCardNum
        return
    }
    val startChar = idCardNum.substring(0, 1)
    val endChar = idCardNum.substring(idCardNum.length - 1)
    var midChar = ""
    for (index in 0..idCardNum.length - 2) {
        midChar += "*"
    }
    text = startChar + midChar + endChar
}

@SuppressLint("SetTextI18n")
fun TextView.phoneText(phoneNum: String) {
    if (phoneNum.length < 8) {
        text = phoneNum
        return
    }
    val startChar = phoneNum.substring(0, 3)
    val endChar = phoneNum.substring(phoneNum.length - 4)
    var midChar = ""
    for (index in 0 until phoneNum.length - 7) {
        midChar += "*"
    }
    text = startChar + midChar + endChar
}

fun EditText.limitMaxTextCount(maxCount: Int) {
    filters = arrayOf(TextLengthLimitInputFilter(maxCount))
}

fun EditText.getEditTextLengthFilter(count: Int): TextLengthLimitInputFilter {
    return TextLengthLimitInputFilter(count)
}

fun EditText.moveSelectionToEnd() {
    if (text.isNotEmpty()) {
        setSelection(text.length)
    }
}

fun EditText.getIDModeValue(): String {
    return text?.toString()?.trim()?.toUpperCase(Locale.getDefault()) ?: ""
}

fun EditText.transLowToUpper() {
    transformationMethod = UpperCaseTransform()
}

internal class UpperCaseTransform : ReplacementTransformationMethod() {
    override fun getOriginal(): CharArray {
        return charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
    }

    override fun getReplacement(): CharArray {
        return charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
    }
}

fun EditText.moneyMode(maxMoney: Float) {
    keyListener = DigitsKeyListener.getInstance("0123456789.")
    val filter = filters.toMutableList()
    filter.add(MoneyInputFilter(maxMoney))
    filters = filter.toTypedArray()
    addTextChangedListener(MoneyFormatTextWatcher(this))
}