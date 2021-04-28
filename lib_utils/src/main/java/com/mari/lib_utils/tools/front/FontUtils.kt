package com.mari.lib_utils.tools.front

import android.content.Context
import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.mari.lib_utils.tools.DisplayUtils

/**
 * @author Alan_Xiong
 * @desc: 富文本工具类
 * @time: 2019/7/26 9:46
 */
class FontUtils private constructor() {
    private object FontHolder {
        private val INSTAMCE = FontUtils()
    }

    /**
     * 改变文字颜色
     * * @param textColor
     *
     * @param content
     * @param changeTextArray
     * @return
     */
    fun changeTextColor(
        textColor: Int,
        content: String,
        vararg changeTextArray: String
    ): SpannableStringBuilder? {
        if (changeTextArray.size == 0) {
            return null
        }
        val builder = SpannableStringBuilder(content)
        var start: Int
        var end = 0
        for (changeText in changeTextArray) {
            start = content.indexOf(changeText, end)
            end = start + changeText.length
            builder.setSpan(
                ForegroundColorSpan(textColor),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return builder
    }

    fun changeTextColor(
        textColor: Int,
        content: String?,
        start: Int,
        end: Int
    ): SpannableStringBuilder {
        val builder = SpannableStringBuilder(content)
        builder.setSpan(
            ForegroundColorSpan(textColor),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

    /**
     * 改变文字大小
     *
     * @param textSize        sp
     * @param content
     * @param changeTextArray
     * @return
     */
    fun changeTextSize(
        context: Context?,
        textSize: Int,
        content: String,
        vararg changeTextArray: String
    ): SpannableStringBuilder? {
        if (changeTextArray.isEmpty()) {
            return null
        }
        val builder = SpannableStringBuilder(content)
        var start: Int
        var end = 0
        val length = changeTextArray.size
        var i = 0
        while (i < length) {
            val changeText = changeTextArray[i]
            start = content.indexOf(changeText, end)
            end = start + changeText.length
            builder.setSpan(
                AbsoluteSizeSpan(
                    DisplayUtils.sp2px(
                        context!!,
                        textSize.toFloat()
                    )
                ),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            i++
        }
        return builder
    }

    fun changeTextSize(
        context: Context?,
        textSize: Int,
        content: String?,
        start: Int,
        end: Int
    ): SpannableStringBuilder {
        val builder = SpannableStringBuilder(content)
        builder.setSpan(
            AbsoluteSizeSpan(
                DisplayUtils.sp2px(
                    context!!,
                    textSize.toFloat()
                )
            ),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

    /**
     * 改变文字大小和颜色
     *
     * @param textColor
     * @param textSize
     * @param content
     * @param changeTextArray
     * @return
     */
    fun changeTextSizeColor(
        context: Context?,
        textColor: Int,
        textSize: Int,
        content: String,
        vararg changeTextArray: String
    ): SpannableStringBuilder? {
        if (changeTextArray.size == 0) {
            return null
        }
        val builder = SpannableStringBuilder(content)
        var start: Int
        var end = 0
        for (changeText in changeTextArray) {
            start = content.indexOf(changeText, end)
            end = start + changeText.length
            builder.setSpan(
                AbsoluteSizeSpan(
                    DisplayUtils.sp2px(
                        context!!,
                        textSize.toFloat()
                    )
                ),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                ForegroundColorSpan(
                    DisplayUtils.sp2px(
                        context,
                        textColor.toFloat()
                    )
                ),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return builder
    }

    /**
     * 改变TextView部分颜色字体大小
     *
     * @param view
     * @param color
     * @param value
     * @param styleValue
     */
    fun changeTextStyle(
        view: AppCompatTextView, @ColorInt color: Int,
        value: String,
        styleValue: String
    ) {
        val redColors = ColorStateList.valueOf(color)
        val spanBuilder = SpannableStringBuilder(value)
        val index = value.indexOf(styleValue)
        spanBuilder.setSpan(
            TextAppearanceSpan(null, 0,
                DisplayUtils.dp2px(
                    view.context,
                    25f
                ), redColors, null),
            index,
            index + styleValue.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        view.setText(spanBuilder)
    }

    fun changeTextSizeColor(
        context: Context?,
        textColor: Int,
        textSize: Int,
        content: String?,
        start: Int,
        end: Int
    ): SpannableStringBuilder {
        val builder = SpannableStringBuilder(content)
        builder.setSpan(
            AbsoluteSizeSpan(
                DisplayUtils.sp2px(
                    context!!,
                    textSize.toFloat()
                )
            ),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        builder.setSpan(
            ForegroundColorSpan(textColor),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

}