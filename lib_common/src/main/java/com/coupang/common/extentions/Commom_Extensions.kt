package com.coupang.common.extentions

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.DecimalFormat

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

fun View.getString(stringResId: Int): String = resources.getString(stringResId)

fun View.showKeyBoard() {
   val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
   imm.showSoftInput(this, 0)
}

fun View.hideKeyBoard(): Boolean {
   return try {
      val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(windowToken, 0)
   } catch (exception: RuntimeException) {
      false
   }
}

fun EditText.getCalculateCount(): Int {
   val content = text.toString()
   var length = 0
   for (i in content) {
      val charAt = i.toInt()
      if (charAt <= 128) {
         length++
      } else {
         length += 2
      }
   }
   return length
}

fun View.setHeight(height: Int) {
   val lp = layoutParams
   lp?.let {
      lp.height = height
      layoutParams = lp
   }
}

fun View.setWidth(width: Int) {
   val lp = layoutParams
   lp?.let {
      lp.width = width
      layoutParams = lp
   }
}

fun View.resize(width: Int, height: Int) {
   val lp = layoutParams
   lp?.let {
      lp.width = width
      lp.height = height
      layoutParams = lp
   }
}

fun View.backgroundColor(color: Int) {
   setBackgroundColor(ContextCompat.getColor(context, color))
}

fun View.backgroundDrawable(id: Int) {
   background = ContextCompat.getDrawable(context, id)
}

val ViewGroup.childrenList: List<View>
   get() = (0 until childCount).map { getChildAt(it) }

fun View.visible() {
   if (visibility != View.VISIBLE) {
      visibility = View.VISIBLE
   }
}

fun View.invisible() {
   if (visibility != View.INVISIBLE) {
      visibility = View.INVISIBLE
   }
}

fun View.gone() {
   if (visibility != View.GONE) {
      visibility = View.GONE
   }
}

inline fun View.showIf(condition: () -> Boolean): View {
   if (visibility != View.VISIBLE && condition()) {
      visibility = View.VISIBLE
   }
   return this
}

inline fun View.goneIf(condition: () -> Boolean): View {
   if (visibility != View.GONE && condition()) {
      visibility = View.GONE
   }
   return this
}

inline fun View.inVisibleIf(condition: () -> Boolean): View {
   if (visibility != View.INVISIBLE && condition()) {
      visibility = View.INVISIBLE
   }
   return this
}

fun String.subZeroAndDot(): String {
   var result = this
   if (result.indexOf(".") > 0) {
      result = result.replace("0+?$".toRegex(), "")
      result = result.replace("[.]$".toRegex(), "")
   }
   return result
}


//截取小数点后几位
fun String.formatNumberAfterPointer(pointer: Int = 0): String {
   val nf = DecimalFormat("##.##")
   nf.minimumFractionDigits = pointer
   return nf.format(this.toDouble())
}

inline val Context.screenWidth: Int
   get() = resources.displayMetrics.widthPixels

inline val Context.screenHeight: Int
   get() = resources.displayMetrics.heightPixels

inline val Context.actionBarHeight: Int
   get() = (screenHeight * 0.068).toInt()

fun Context.dip2px(dpValue: Int): Int {
   val scale = resources.displayMetrics.density
   return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
   val fontScale = resources.displayMetrics.scaledDensity
   return (pxValue / fontScale + 0.5f).toInt()
}

fun Context.px2dip(pxValue: Float): Int {
   val scale = resources.displayMetrics.density
   return (pxValue / scale + 0.5f).toInt()
}

inline val Context.inflater: LayoutInflater
   get() = LayoutInflater.from(this)

fun <T> isEmpty(list: List<T>?): Boolean {
   return list?.isEmpty() ?: true
}

