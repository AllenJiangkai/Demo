package com.coupang.common.extentions.textview.filter

import android.text.InputFilter
import android.text.Spanned
import android.util.Log

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
class TextLengthLimitInputFilter(private val mMaxLength: Int) : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        var count = 0
        var dindex = 0
        while (count <= mMaxLength && dindex < dest.length) {
            val c = dest[dindex++]
            count += if (c.toInt() < 128) {
                1
            } else {
                2
            }
        }
        if (count > mMaxLength) {
            return dest.subSequence(0, dindex - 1)
        }
        var sindex = 0
        while (count <= mMaxLength && sindex < source.length) {
            val c = source[sindex++]
            count += if (c.toInt() < 128) {
                1
            } else {
                2
            }
        }
        if (count > mMaxLength) {
            sindex--
        }
        Log.e("allen","$sindex")
        return source.subSequence(0, sindex)
    }
}