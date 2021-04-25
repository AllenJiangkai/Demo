package com.mari.uang.module.home.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.coupang.common.extentions.gone
import com.coupang.common.extentions.visible
import com.mari.uang.MyApplication
import com.mari.uang.R
import com.mari.uang.module.home.dto.DialogInfo
import com.mari.uang.module.home.widget.dialog.StyleUtil.buildTextView
import kotlinx.android.synthetic.main.dialog_product.*

class ProductDialog(context: Context) : Dialog(context, R.style.TipsDialogTheme) {
    private var rootView: View
    private lateinit var clickSubmitListener: (data :String) -> Unit
    var bean: DialogInfo? = null
    private val adapter by lazy { DialogAdapter() }


    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.dialog_product, null, false)
        setContentView(rootView)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(
            (MyApplication.screenWidth * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(0x00000000))
        window?.setWindowAnimations(R.style.dia_com_anim)
    }

    inline fun show(func: ProductDialog.() -> Unit): ProductDialog {
        this.func()
        this.show()
        return this
    }

    fun onClickSubmitListener(callback: (data :String) -> Unit): ProductDialog {
        this.clickSubmitListener = callback
        return this
    }


    fun setDialogBean(bean: DialogInfo): ProductDialog {
        this.bean = bean
        return this
    }

    fun initUi(): ProductDialog {

        bean?.apply {
            if (title != null) {
                tv_title.visible()
                buildTextView(tv_title, title)
            } else {
                tv_title.gone()
            }

            if (showClose == 1) {
                layout_close.visible()
            } else {
                layout_close.gone()
            }
            iv_close.setOnClickListener {
                dismiss()
            }
            setCancelable(cancelable == 1)
            setCanceledOnTouchOutside(cancelableOutside == 1)
            if (list != null && list?.isNotEmpty()!!) {
                recycler_content.setMaxHeight((MyApplication.screenHeight * 0.5).toInt())
                recycler_content.layoutManager = LinearLayoutManager(context)
                recycler_content.adapter = adapter
                adapter.setNewData(convertUI())
                adapter.setMyDialog(this@ProductDialog)
            }
        }

        return this
    }


    private fun convertUI(): ArrayList<MultiItemEntity> {
        val result = ArrayList<MultiItemEntity>()
        bean?.apply {
            list?.forEach {
                when (it.getString("type")) {
                    "message" -> {
                        result.add(DialogAdapterItem(DialogAdapterItem.MESSAGE,it))
                    }
                    "singleBtn" -> {
                        result.add(DialogAdapterItem(DialogAdapterItem.SINGLE_BTN,it))
                    }
                    "info" -> {
                        result.add(DialogAdapterItem(DialogAdapterItem.INFO,it))
                    }
                    "image" -> {
                        result.add(DialogAdapterItem(DialogAdapterItem.IMG,it))
                    }
                    "multipleBtn" -> {
                        result.add(DialogAdapterItem(DialogAdapterItem.MULTIPLE_BTN,it))
                    }
                }
            }

        }
        return result

    }


}