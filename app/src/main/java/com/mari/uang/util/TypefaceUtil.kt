package com.mari.uang.util

import android.graphics.Typeface
import android.widget.TextView
import com.coupang.common.utils.ContextUtils

object TypefaceUtil {
    var tf: Typeface? =
        Typeface.createFromAsset(ContextUtils.getSharedContext().assets, "bebas.ttf")

   fun setTextTypeface(view : TextView){
       view.typeface = tf
   }
}