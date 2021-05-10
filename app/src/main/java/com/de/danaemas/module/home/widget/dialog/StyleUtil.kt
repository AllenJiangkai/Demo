package com.de.danaemas.module.home.widget.dialog

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.de.danaemas.module.home.dto.DialogButtonInfo
import com.de.danaemas.module.home.dto.DialogTextInfo
import com.de.danaemas.util.RouterUtil
import java.util.regex.Pattern

object StyleUtil{

    fun buildTextView(tv: TextView?, entry: DialogTextInfo?) {
        if (tv == null || entry == null) return
        tv.text = entry.text
        if (entry.size > 0) {
            tv.textSize = entry.size.toFloat()
        }
        if ( isColor(entry.color)) {
            tv.setTextColor(Color.parseColor(entry.color))
        }
    }
    fun isColor(color: String?): Boolean {
        if (TextUtils.isEmpty(color)) return false
        val p = Pattern.compile("#[0-9a-fA-F]{6,8}$")
        val m = p.matcher(color)
        return m.matches()
    }

    fun buildButton(tv: TextView?, entry: DialogButtonInfo?,dialog: ProductDialog?,categoryBlock: (data: String) -> Unit) {
        if (tv == null || entry == null) return
        tv.text = entry.text
        if (entry.size > 0) {
            tv.textSize = entry.size.toFloat()
        }
        if (isColor(entry.color)) {
            tv.setTextColor(Color.parseColor(entry.color))
        }
        if (isColor(entry.bgColor)) {
            try {
                tv.background.mutate().setTint(Color.parseColor(entry.bgColor))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        tv.setOnClickListener(View.OnClickListener { v: View ->
            when (entry.type) {
                1 ->{
                    categoryBlock(entry.pid?:"")
                }
                2 -> { RouterUtil.router(v.context, entry.url) }
            }
            if(entry.close==1){
                dialog?.dismiss()
            }
        })
    }

}