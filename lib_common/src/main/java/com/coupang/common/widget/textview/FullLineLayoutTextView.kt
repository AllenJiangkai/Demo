package com.coupang.common.widget.textview

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Paint.Style
import android.graphics.Rect
import android.text.Spannable
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.DynamicDrawableSpan
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.appcompat.widget.AppCompatTextView
import java.lang.ref.SoftReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * @author Allen
 * @date 2020-05-27.
 * description：module：图文混排TextView，请使用[.setMText]
 */


class FullLineLayoutTextView : AppCompatTextView {
    /**
     * 存储当前文本内容，每个item为一行
     */
    private var contentLineList = ArrayList<LINE>()
    /**
     * 存储当前文本内容,每个item为一个字符或者一个SpanObject
     */
    private val contentElementList = ArrayList<Any>()
    /**
     * 用于测量字符宽度
     */
    private val mPaint = TextPaint()
    /**
     * 用于测量span高度
     */
    private val mSpanFmInt = Paint.FontMetricsInt()
    /**
     * 临时使用,以免在onDraw中反复生产新对象
     */
    private val mFontMetrics = FontMetrics()

    // private float lineSpacingMult = 0.5f;
    private var textColor = Color.parseColor("#ff222222")
    // 行距
    private var lineSpacing: Float = 0.toFloat()
    /**
     * 行距，单位dp
     */
    var lineSpacingDP = 5
        set(lineSpacingDP) {
            field = lineSpacingDP
            lineSpacing = dip2px(context, lineSpacingDP.toFloat()).toFloat()
        }
    /**
     * 段间距,-1为默认
     */
    private var paragraphSpacing = -1
    /**
     * 最大宽度
     */
    private var mMaxWidth: Int = 0
    /**
     * 只有一行时的宽度
     */
    private var oneLineWidth = -1
    /**
     * 已绘的行中最宽的一行的宽度
     */
    private var lineWidthMax = -1f

    /**
     * 是否使用默认[.onMeasure]和[.onDraw]
     */
    private var useDefault = false

    private var mText: CharSequence = ""

    private var mMinHeight: Int = 0
    /**
     * 用以获取屏幕高宽
     */
    private var displayMetrics: DisplayMetrics? = null
    /**
     * [android.text.style.BackgroundColorSpan]用
     */
    private val textBgColorPaint = Paint()
    /**
     * [android.text.style.BackgroundColorSpan]用
     */
    private val textBgColorRect = Rect()

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    fun init(context: Context) {
        mPaint.isAntiAlias = true
        lineSpacing = dip2px(context, this.lineSpacingDP.toFloat()).toFloat()
        mMinHeight = dip2px(context, 30f)
        displayMetrics = DisplayMetrics()
    }

    override fun setMaxWidth(maxpixels: Int) {
        super.setMaxWidth(maxpixels)
        mMaxWidth = maxpixels
    }

    override fun setMinHeight(minHeight: Int) {
        super.setMinHeight(minHeight)
        this.mMinHeight = minHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (useDefault) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        var width = 0
        var height = 0
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        when (widthMode) {
            MeasureSpec.EXACTLY -> width = widthSize
            MeasureSpec.AT_MOST -> width = widthSize
            MeasureSpec.UNSPECIFIED -> {
                (context as Activity).windowManager.defaultDisplay
                        .getMetrics(displayMetrics)
                width = displayMetrics!!.widthPixels
            }
            else -> {
            }
        }
        if (mMaxWidth > 0) {
            width = min(width, mMaxWidth)
        }
        mPaint.textSize = this.textSize
        mPaint.color = textColor
        val realHeight = measureContentHeight(width)
        // 如果实际行宽少于预定的宽度，减少行宽以使其内容横向居中
        val leftPadding = compoundPaddingLeft
        val rightPadding = compoundPaddingRight
        width = min(width, lineWidthMax.toInt() + leftPadding + rightPadding)
        if (oneLineWidth > -1) {
            width = oneLineWidth
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> height = heightSize
            MeasureSpec.AT_MOST -> height = realHeight
            MeasureSpec.UNSPECIFIED -> height = realHeight
            else -> {
            }
        }
        height += compoundPaddingTop + compoundPaddingBottom
        height = max(height, mMinHeight)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        if (useDefault) {
            super.onDraw(canvas)
            return
        }
        if (contentLineList.isEmpty()) {
            return
        }
        var width: Int
        var obj: Any
        val leftPadding = compoundPaddingLeft
        val topPadding = compoundPaddingTop
        var height = 0f + topPadding.toFloat() + lineSpacing
        // 只有一行时
        if (oneLineWidth != -1) {
            height = measuredHeight / 2 - contentLineList[0].height / 2
        }
        for (contentOfLine in contentLineList) {
            // 绘制一行
            var realDrawWidth = leftPadding.toFloat()
            /** 是否换新段落  */
            var newParagraph = false
            for (index in contentOfLine.line.indices) {
                obj = contentOfLine.line[index]
                width = contentOfLine.widthList[index]
                mPaint.getFontMetrics(mFontMetrics)
                val x = realDrawWidth
                val y = height + contentOfLine.height - mPaint.fontMetrics.descent
                val top = y - contentOfLine.height
                val bottom = y + mFontMetrics.descent
                if (obj is String) {
                    canvas.drawText(obj, realDrawWidth, y, mPaint)
                    realDrawWidth += width.toFloat()
                    if (obj.endsWith("\n") && index == contentOfLine.line.size - 1) {
                        newParagraph = true
                    }
                } else if (obj is SpanObject) {
                    when (val span = obj.span) {
                        is DynamicDrawableSpan -> {
                            val start = (mText as Spannable).getSpanStart(span)
                            val end = (mText as Spannable).getSpanEnd(span)
                            span.draw(canvas, mText, start,
                                    end, x.toInt().toFloat(), top.toInt(), y.toInt(), bottom.toInt(),
                                    mPaint)
                            realDrawWidth += width.toFloat()
                        }
                        is BackgroundColorSpan -> {
                            textBgColorPaint.color = span.backgroundColor
                            textBgColorPaint.style = Style.FILL
                            textBgColorRect.left = realDrawWidth.toInt()
                            val textHeight = textSize.toInt()
                            textBgColorRect.top = (height + contentOfLine.height - textHeight.toFloat() - mFontMetrics.descent).toInt()
                            textBgColorRect.right = textBgColorRect.left + width
                            textBgColorRect.bottom = ((height + contentOfLine.height + lineSpacing) - mFontMetrics.descent).toInt()
                            canvas.drawRect(textBgColorRect, textBgColorPaint)
                            canvas.drawText(obj.source!!.toString(), realDrawWidth, height + contentOfLine.height - mFontMetrics.descent, mPaint)
                            realDrawWidth += width.toFloat()
                        }
                        else -> { // 做字符串处理
                            canvas.drawText(obj.source!!.toString(), realDrawWidth, height + contentOfLine.height - mFontMetrics.descent, mPaint)
                            realDrawWidth += width.toFloat()
                        }
                    }
                }
            }
            // 如果要绘制段间距
            height += if (newParagraph) {
                contentOfLine.height + paragraphSpacing
            } else {
                contentOfLine.height + lineSpacing
            }
        }
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        textColor = color
    }

    /**
     * 用于带ImageSpan的文本内容所占高度测量
     *
     * @param width
     * 预定的宽度
     * @return 所需的高度
     */
    private fun measureContentHeight(width: Int): Int {
        var tempWidth = width
        val cachedHeight = getCachedData(mText.toString(), tempWidth)

        if (cachedHeight > 0) {
            return cachedHeight
        }

        // 已绘的宽度
        var obWidth = 0f
        var obHeight = 0f

        val textSize = this.textSize
        val fontMetrics = mPaint.fontMetrics
        // 行高
        var lineHeight = fontMetrics.bottom - fontMetrics.top
        // 计算出的所需高度
        var height = lineSpacing

        val leftPadding = compoundPaddingLeft
        val rightPadding = compoundPaddingRight

        var drawWidth = 0f

        var splitFlag = false// BackgroundColorSpan拆分用

        tempWidth = tempWidth - leftPadding - rightPadding

        oneLineWidth = -1

        contentLineList.clear()

        var sb: StringBuilder

        var line = LINE()

        var i = 0
        while (i < contentElementList.size) {
            var ob = contentElementList[i]
            if (ob is String) {
                obWidth = mPaint.measureText(ob)
                obHeight = textSize
                if ("\n" == ob) { // 遇到"\n"则换行
                    obWidth = tempWidth - drawWidth
                }
            } else if (ob is SpanObject) {
                val span = ob.span
                if (span is DynamicDrawableSpan) {
                    val start = (mText as Spannable).getSpanStart(span)
                    val end = (mText as Spannable).getSpanEnd(span)
                    obWidth = span.getSize(paint,
                            mText, start, end, mSpanFmInt).toFloat()
                    obHeight = (abs(mSpanFmInt.top) + abs(mSpanFmInt.bottom)).toFloat()
                    if (obHeight > lineHeight) {
                        lineHeight = obHeight
                    }
                } else if (span is BackgroundColorSpan) {
                    val str = ob.source!!.toString()
                    obWidth = mPaint.measureText(str)
                    obHeight = textSize

                    // 如果太长,拆分
                    var k = str.length - 1
                    while (tempWidth - drawWidth < obWidth) {
                        obWidth = mPaint.measureText(str.substring(0, k--))
                    }
                    if (k < str.length - 1) {
                        splitFlag = true
                        val so1 = SpanObject()
                        so1.start = ob.start
                        so1.end = so1.start + k
                        so1.source = str.substring(0, k + 1)
                        so1.span = ob.span

                        val so2 = SpanObject()
                        so2.start = so1.end
                        so2.end = ob.end
                        so2.source = str.substring(k + 1, str.length)
                        so2.span = ob.span

                        ob = so1
                        contentElementList[i] = so2
                        i--
                    }
                }// 做字符串处理
                else {
                    val str = ob.source!!.toString()
                    obWidth = mPaint.measureText(str)
                    obHeight = textSize
                }
            }

            // 这一行满了，存入contentList,新起一行
            if (tempWidth - drawWidth < obWidth || splitFlag) {
                splitFlag = false
                contentLineList.add(line)

                if (drawWidth > lineWidthMax) {
                    lineWidthMax = drawWidth
                }
                drawWidth = 0f
                // 判断是否有分段
                val objNum = line.line.size
                height += if (paragraphSpacing > 0
                        && objNum > 0
                        && line.line[objNum - 1] is String
                        && "\n" == line.line[objNum - 1]) {
                    line.height + paragraphSpacing
                } else {
                    line.height + lineSpacing
                }
                lineHeight = obHeight
                line = LINE()
            }

            drawWidth += obWidth

            if (ob is String && line.line.size > 0
                    && line.line[line.line.size - 1] is String) {
                val size = line.line.size
                sb = StringBuilder()
                sb.append(line.line[size - 1])
                sb.append(ob)
                ob = sb.toString()
                obWidth += line.widthList[size - 1]
                line.line[size - 1] = ob
                line.widthList[size - 1] = obWidth.toInt()
                line.height = lineHeight.toInt().toFloat()
            } else {
                line.line.add(ob)
                line.widthList.add(obWidth.toInt())
                line.height = lineHeight.toInt().toFloat()
            }
            i++
        }
        if (drawWidth > lineWidthMax) {
            lineWidthMax = drawWidth
        }
        if (line.line.size > 0) {
            contentLineList.add(line)
            height += lineHeight + lineSpacing
        }
        if (contentLineList.size <= 1) {
            oneLineWidth = drawWidth.toInt() + leftPadding + rightPadding
            height = lineSpacing + lineHeight + lineSpacing
        }
        cacheData(tempWidth, height.toInt())
        return height.toInt()
    }

    /**
     * 获取缓存的测量数据，避免多次重复测量
     *
     * @param text
     * @param width
     * @return height
     */
    private fun getCachedData(text: String, width: Int): Int {
        val cache = measuredData[text] ?: return -1
        val md = cache.get()
        return if (md != null && md.textSize == this.textSize && width == md.width) {
            lineWidthMax = md.lineWidthMax
            contentLineList = cloneData(md.contentList)
            oneLineWidth = md.oneLineWidth

            val sb = StringBuilder()
            for (i in contentLineList.indices) {
                val line = contentLineList[i]
                sb.append(line.toString())
            }
            md.measuredHeight
        } else {
            -1
        }
    }

    private fun cloneData(source: ArrayList<LINE>?): ArrayList<LINE> {
        return source?.let {
            val result = arrayListOf<LINE>()
            it.forEach { item ->
                result.add(LINE().apply {
                    line = item.line
                    widthList = item.widthList
                    height = item.height
                })
            }
            result
        } ?: arrayListOf()
    }

    /**
     * 缓存已测量的数据
     *
     * @param width
     * @param height
     */
    private fun cacheData(width: Int, height: Int) {
        val md = MeasuredData()
        md.contentList = cloneData(contentLineList)
        md.textSize = this.textSize
        md.lineWidthMax = lineWidthMax
        md.oneLineWidth = oneLineWidth
        md.measuredHeight = height
        md.width = width
        md.hashIndex = ++hashIndex

        val sb = StringBuilder()
        for (i in contentLineList.indices) {
            val line = contentLineList[i]
            sb.append(line.toString())
        }
        val cache = SoftReference(md)
        measuredData[mText.toString()] = cache
    }

    /**
     * 用本函数代替[.setText]
     *
     * @param cs
     */
    fun setMText(cs: CharSequence) {
        mText = cs
        contentElementList.clear()
        val isList = ArrayList<SpanObject>()
        useDefault = false
        if (cs is Spannable) {
            val spans = cs.getSpans(0, cs.length, CharacterStyle::class.java)
            for (i in spans.indices) {
                val s = cs.getSpanStart(spans[i])
                val e = cs.getSpanEnd(spans[i])
                val iS = SpanObject()
                iS.span = spans[i]
                iS.start = s
                iS.end = e
                iS.source = cs.subSequence(s, e)
                isList.add(iS)
            }
        }
        // 对span进行排序，以免不同种类的span位置错乱
        val spanArray = isList.toTypedArray()
        Arrays.sort<SpanObject>(spanArray, 0, spanArray.size, SpanObjectComparator())
        isList.clear()
        for (i in spanArray.indices) {
            isList.add(spanArray[i])
        }
        val str = cs.toString()
        var i = 0
        var j = 0
        while (i < cs.length) {
            if (j < isList.size) {
                val `is` = isList[j]
                if (i < `is`.start) {
                    val cp = str.codePointAt(i)
                    // 支持增补字符
                    if (Character.isSupplementaryCodePoint(cp)) {
                        i += 2
                    } else {
                        i++
                    }
                    contentElementList.add(String(Character.toChars(cp)))
                } else if (i >= `is`.start) {
                    contentElementList.add(`is`)
                    j++
                    i = `is`.end
                }
            } else {
                val cp = str.codePointAt(i)
                if (Character.isSupplementaryCodePoint(cp)) {
                    i += 2
                } else {
                    i++
                }
                contentElementList.add(String(Character.toChars(cp)))
            }
        }
        requestLayout()
    }

    fun setUseDefault(useDefault: Boolean) {
        this.useDefault = useDefault
        if (useDefault) {
            this.text = mText
            this.setTextColor(textColor)
        }
    }

    fun setParagraphSpacingDP(paragraphSpacingDP: Int) {
        paragraphSpacing = dip2px(context!!, paragraphSpacingDP.toFloat())
    }

    /**
     * 存储Span对象及相关信息
     */
    internal inner class SpanObject {
        var span: Any? = null
        var start: Int = 0
        var end: Int = 0
        var source: CharSequence? = null
    }

    /**
     * 对SpanObject进行排序
     */
    private inner class SpanObjectComparator : Comparator<SpanObject> {

        override fun compare(lhs: SpanObject, rhs: SpanObject): Int {
            return lhs.start - rhs.start
        }
    }

    /**
     * 存储测量好的一行数据
     */
    private inner class LINE {
        var line = ArrayList<Any>()
        var widthList = ArrayList<Int>()
        var height: Float = 0.toFloat()

        override fun toString(): String {
            val sb = StringBuilder("height:$height   ")
            for (i in line.indices) {
                sb.append(line[i].toString() + ":" + widthList[i])
            }
            return sb.toString()
        }
    }

    /**
     * 缓存的数据
     */
    private inner class MeasuredData {
        var measuredHeight: Int = 0
        var textSize: Float = 0.toFloat()
        var width: Int = 0
        var lineWidthMax: Float = 0.toFloat()
        var oneLineWidth: Int = 0
        var hashIndex: Int = 0
        var contentList: ArrayList<LINE>? = null
    }

    companion object {
        /**
         * 缓存测量过的数据
         */
        private val measuredData = HashMap<String, SoftReference<MeasuredData>>()
        private var hashIndex = 0

        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}
