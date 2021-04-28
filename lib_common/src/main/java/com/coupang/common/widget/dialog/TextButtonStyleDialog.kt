package com.coupang.common.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import com.coupang.common.extentions.dip2px
import com.coupang.common.widget.dialog.ext.invokeAll
import com.coupang.common.widget.textview.FullLineLayoutTextView
import com.mari.common.R
import kotlin.math.min


typealias MDDialogCallback = (Dialog) -> Unit
/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

class TextButtonStyleDialog(context: Context) : Dialog(context, R.style.common_Light) {

    var radius: Float? = null
        internal set
    var autoDismissEnabled: Boolean = true

    private var rootView: View
    private lateinit var dialogRoot: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var messageView: FullLineLayoutTextView
    private lateinit var contentRoot: FrameLayout
    private lateinit var btnNegative: TextView
    private lateinit var btnPositive: TextView
    private lateinit var spaceOfButton: View
    private lateinit var llBottomBtnRoot : LinearLayout

    private val preShowListeners = mutableListOf<MDDialogCallback>()
    private val showListeners = mutableListOf<MDDialogCallback>()
    private val dismissListeners = mutableListOf<MDDialogCallback>()
    private val cancelListeners = mutableListOf<MDDialogCallback>()

    private val positiveListeners = mutableListOf<MDDialogCallback>()
    private val negativeListeners = mutableListOf<MDDialogCallback>()

    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.common_dialog_text_button_style, null, false)
        setContentView(rootView)
        findView()
        initWidgetValueAndVisibility()
        invalidateBackgroundColorAndRadius()
    }

    private fun initWidgetValueAndVisibility() {
        titleView.visibility = View.GONE
        messageView.visibility = View.GONE
        btnNegative.visibility = View.GONE
        btnPositive.visibility = View.GONE
        spaceOfButton.visibility = View.GONE
    }

    private fun findView() {
        dialogRoot = rootView.findViewById(R.id.dialogRoot)
        titleView = rootView.findViewById(R.id.tvDialogTitle)
        messageView = rootView.findViewById(R.id.tvDialogMessage)
        contentRoot = rootView.findViewById(R.id.flContentRoot)
        btnNegative = rootView.findViewById(R.id.btnDialogNegative)
        btnPositive = rootView.findViewById(R.id.btnDialogPositive)
        spaceOfButton = rootView.findViewById(R.id.spaceOfBtn)
        llBottomBtnRoot = rootView.findViewById(R.id.llBottomBtnRoot)
        btnNegative.setOnClickListener {
            this.negativeListeners.invokeAll(this)
            if (autoDismissEnabled) {
                dismiss()
            }
        }
        btnPositive.setOnClickListener {
            this.positiveListeners.invokeAll(this)
            if (autoDismissEnabled) {
                dismiss()
            }
        }
    }


    fun title(text: String?): TextButtonStyleDialog {
        text?.let {
            if (it.isNotEmpty()) {
                titleView.visibility = View.VISIBLE
                titleView.text = it
            }
        }
        return this
    }

    fun message(text: String?, textGravity: Int = Gravity.CENTER): TextButtonStyleDialog {
        text?.let {
            if (it.isNotEmpty()) {
                messageView.visibility = View.VISIBLE
                messageView.setTextColor(Color.parseColor("#222222"))
                messageView.minHeight = context.dip2px(44)
                messageView.lineSpacingDP = 10
                messageView.gravity = textGravity
                messageView.setMText(it)
            }
        }
        return this
    }

    fun customView(view: View, paramsLayout: FrameLayout.LayoutParams?): TextButtonStyleDialog {
        messageView.visibility = View.GONE
        contentRoot.addView(view, paramsLayout
                ?: FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT).also {
                    it.gravity = Gravity.CENTER
                })
        return this
    }

    fun negativeButton(text: String?, isShow: Boolean = true, click: MDDialogCallback?): TextButtonStyleDialog {
        text?.let {
            if (it.isNotEmpty()) {
                btnNegative.text = it
            }
        }
        click?.let { this.negativeListeners.add(it) }
        btnNegative.visibility = if (isShow) View.VISIBLE else View.GONE
        return this
    }

    fun positiveButton(text: String?, isShow: Boolean = true,
                       @ColorRes textColor: Int? = null,
                       click: MDDialogCallback? = null): TextButtonStyleDialog {
        text?.let {
            if (it.isNotEmpty()) {
                btnPositive.text = it
            }
        }

        textColor?.let {
            btnPositive.setTextColor(it)
        }

        click?.let { this.positiveListeners.add(it) }
        btnPositive.visibility = if (isShow) View.VISIBLE else View.GONE
        return this
    }


    fun cornerRadius(dipValue: Float?): TextButtonStyleDialog {
        dipValue?.let {
            radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.resources.displayMetrics)
            invalidateBackgroundColorAndRadius()
        }
        return this
    }

    fun maxWidth(width: Int?): TextButtonStyleDialog {
        width?.let {
            setWindowConstraints(it)
        }
        return this
    }

    fun noAutoDismiss(): TextButtonStyleDialog {
        this.autoDismissEnabled = false
        return this
    }

    fun cancelable(cancelable: Boolean): TextButtonStyleDialog {
        this.setCancelable(cancelable)
        return this
    }

    fun cancelOnTouchOutside(cancelable: Boolean): TextButtonStyleDialog {
        this.setCanceledOnTouchOutside(cancelable)
        return this
    }

    fun onShow(callback: MDDialogCallback): TextButtonStyleDialog {
        this.showListeners.add(callback)
        if (this.isShowing) {
            this.showListeners.invokeAll(this)
        }
        setOnShowListener {
            this.showListeners.invokeAll(this)
        }
        return this
    }

    fun onDismiss(callback: MDDialogCallback): TextButtonStyleDialog {
        this.dismissListeners.add(callback)
        setOnDismissListener {
            this.dismissListeners.invokeAll(this)
        }
        return this
    }

    fun onCancel(callback: MDDialogCallback): TextButtonStyleDialog {
        this.cancelListeners.add(callback)
        setOnCancelListener { cancelListeners.invokeAll(this) }
        return this
    }

    override fun show() {
        setWindowConstraints(0)
        preShow()
        super.show()
    }

    inline fun show(func: TextButtonStyleDialog.() -> Unit): TextButtonStyleDialog {
        this.func()
        this.preShow()
        this.show()
        return this
    }

    fun preShow() {
        spaceOfButton.visibility = if (btnNegative.visibility == View.VISIBLE && btnPositive.visibility == View.VISIBLE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        llBottomBtnRoot.visibility = if (btnNegative.visibility == View.VISIBLE || btnPositive.visibility == View.VISIBLE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        if (btnNegative.visibility == View.VISIBLE && btnPositive.visibility == View.VISIBLE) {
            btnNegative.setBackgroundResource(R.drawable.common_selector_text_btn_style_dialog_negative_button_background)
            btnPositive.setBackgroundResource(R.drawable.common_selector_text_btn_style_dialog_positive_button_background)
        } else {
            btnNegative.setBackgroundResource(R.drawable.common_selector_text_btn_style_dialog_single_button_background)
            btnPositive.setBackgroundResource(R.drawable.common_selector_text_btn_style_dialog_single_button_background)
        }
        this.preShowListeners.invokeAll(this)
    }

    private fun setWindowConstraints(maxWidth: Int) {
        if (maxWidth == 0) {
            return
        }
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val res = context.resources
        window?.windowManager?.let {
            val point = Point()
            it.defaultDisplay.getSize(point)
            val windowWidthAndHeight = Pair(point.x, point.y)
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)

                val windowHorizontalPadding = res.getDimensionPixelSize(R.dimen.common_dialog_horizontal_margin)
                val calculatedWidth = windowWidthAndHeight.first - windowHorizontalPadding * 2
                val actualMaxWidth = res.getDimensionPixelSize(R.dimen.common_dialog_max_width)
                width = min(actualMaxWidth, calculatedWidth)
            }
            window?.attributes = lp
        }
    }

    private fun invalidateBackgroundColorAndRadius() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tempRadius = radius
                ?: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
        rootView.background = GradientDrawable().apply {
            cornerRadius = tempRadius
            setColor(Color.WHITE)
        }
    }
}