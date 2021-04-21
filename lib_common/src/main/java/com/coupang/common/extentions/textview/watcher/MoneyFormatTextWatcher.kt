package com.coupang.common.extentions.textview.watcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
class MoneyFormatTextWatcher(private val editText: EditText) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            var result = it
            // 判断小数点后只能输入两位
            if (result.toString().contains(".")) {
                if (result.length - 1 - result.toString().indexOf(".") > 2) {
                    result = result.toString().subSequence(0, result.toString().indexOf(".") + 3)
                    editText.setText(result)
                    editText.setSelection(editText.text.length)
                }
            }

            //如果第一个数字为0，第二个不为点，就不允许输入
            if (result.toString().startsWith("0") && result.toString().trim().length > 1) {
                if (result.toString().substring(1, 2) != ".") {
                    editText.setText(result.replaceFirst(Regex("0"),""))
                    editText.setSelection(editText.text.length)
                    return
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        val value = editText.text.toString().trim()
        if (value.isNotEmpty()) {
            if (value.substring(0, 1) == ".") {
                editText.text = Editable.Factory.getInstance().newEditable("0$value")
                editText.setSelection(editText.text.length)
            }
        }
    }
}