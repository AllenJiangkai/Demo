package com.mari.uang.module.home.widget.dialog

import android.graphics.Paint
import android.text.TextUtils
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mari.uang.R
import com.mari.uang.module.home.widget.dialog.DialogAdapterItem.Companion.IMG
import com.mari.uang.module.home.widget.dialog.DialogAdapterItem.Companion.INFO
import com.mari.uang.module.home.widget.dialog.DialogAdapterItem.Companion.MESSAGE
import com.mari.uang.module.home.widget.dialog.DialogAdapterItem.Companion.MULTIPLE_BTN
import com.mari.uang.module.home.widget.dialog.DialogAdapterItem.Companion.SINGLE_BTN
import com.mari.uang.module.home.widget.dialog.StyleUtil.buildButton
import com.mari.uang.module.home.widget.dialog.dto.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.coupang.common.utils.GlideLoadUtils.loadImage
import com.coupang.common.utils.dip2px
import kotlinx.android.synthetic.main.activity_order.view.*


class DialogAdapter(data: MutableList<MultiItemEntity>? = null) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    fun onClickSubmitListener(callback: (data :String) -> Unit) {
        this.clickSubmitListener = callback
    }

    private lateinit var clickSubmitListener: (data :String) -> Unit


    init {
        addItemType(MESSAGE, R.layout.item_message)
        addItemType(SINGLE_BTN, R.layout.item_single_btn)
        addItemType(MULTIPLE_BTN, R.layout.item_multiple_btn)
        addItemType(INFO, R.layout.item_info)
        addItemType(IMG, R.layout.item_img)

    }

    var dialog:ProductDialog?=null

    fun setMyDialog(dialog: ProductDialog){
        this.dialog=dialog
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity?) {
        val homeEntity = item as DialogAdapterItem<*>
        when (homeEntity.itemType) {
            MESSAGE -> {
                convertMessage(helper, homeEntity)
            }
            SINGLE_BTN -> {
                convertSingleBt(helper, homeEntity)
            }
            MULTIPLE_BTN -> {
                convertDoubleBt(helper, homeEntity)
            }
            INFO -> {
                convertInfo(helper, homeEntity)
            }
            IMG -> {
                convertImage(helper, homeEntity)
            }
        }
    }

    private fun convertMessage(helper: BaseViewHolder, item: DialogAdapterItem<*>) {
        helper.run {
            val bean = item.data as JSONObject
            val entry: MessageInfo = JSON.parseObject(bean.toJSONString(), MessageInfo::class.java)

            val params = itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dip2px(entry.topMargin)
            params.bottomMargin = dip2px(entry.bottomMargin)
            StyleUtil.buildTextView(getView(R.id.tv_msg), entry.msg)
            getView<TextView>(R.id.tv_msg).apply {
                viewTreeObserver.addOnGlobalLayoutListener(OnTvGlobalLayoutListener(this))
            }

        }
    }

    private fun convertInfo(helper: BaseViewHolder, item: DialogAdapterItem<*>) {
        helper.run {
            val bean = item.data as JSONObject
            val entry: Info = JSON.parseObject(bean.toJSONString(), Info::class.java)

            val params = itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dip2px(entry.topMargin)
            params.bottomMargin = dip2px(entry.bottomMargin)
            StyleUtil.buildTextView(getView(R.id.tv_title), entry.title)
            StyleUtil.buildTextView(getView(R.id.tv_info), entry.info)
        }
    }

    private fun convertImage(helper: BaseViewHolder, item: DialogAdapterItem<*>) {
        helper.run {
            val bean = item.data as JSONObject
            val entry: ImgInfo = JSON.parseObject(bean.toJSONString(), ImgInfo::class.java)

            val params = itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dip2px(entry.topMargin)
            params.bottomMargin = dip2px(entry.bottomMargin)
            loadImage(mContext, entry.img, getView<ImageView>(R.id.iv_img))
        }
    }

    private fun convertSingleBt(helper: BaseViewHolder, item: DialogAdapterItem<*>) {
        helper.run {
            val bean = item.data as JSONObject
            val entry: SingleBt = JSON.parseObject(bean.toJSONString(), SingleBt::class.java)

            val params = itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dip2px(entry.topMargin)
            params.bottomMargin = dip2px(entry.bottomMargin)

            if (entry.height > 0) {
                getView<TextView>(R.id.tv_btn) .layoutParams.height = dip2px(entry.height)
            }
            buildButton(getView<TextView>(R.id.tv_btn), entry.btn, dialog,::onClickCallBack)

            if (entry.marginHorizontal > 0) {
                getView<TextView>(R.id.tv_btn) .layoutParams.height = dip2px(entry.height)
                val btnParams  = getView<TextView>(R.id.tv_btn) .layoutParams as (LinearLayout.LayoutParams)
                btnParams.leftMargin=dip2px(entry.marginHorizontal)
                btnParams.rightMargin=dip2px(entry.marginHorizontal)
            }
        }
    }
    private fun convertDoubleBt(helper: BaseViewHolder, item: DialogAdapterItem<*>) {
        helper.run {
            val bean = item.data as JSONObject
            val entry: DoubleBt = JSON.parseObject(bean.toJSONString(), DoubleBt::class.java)

            val params = itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dip2px(entry.topMargin)
            params.bottomMargin = dip2px(entry.bottomMargin)
            if (entry.height > 0) {
                getView<TextView>(R.id.tv_btn1) .layoutParams.height = dip2px(entry.height)
                getView<TextView>(R.id.tv_btn2) .layoutParams.height = dip2px(entry.height)
            }
            if (entry.list == null || entry.list?.size?:0 < 2) return
            buildButton(getView<TextView>(R.id.tv_btn1), entry.list!![0], dialog,::onClickCallBack)
            buildButton(getView<TextView>(R.id.tv_btn2), entry.list!![1], dialog,::onClickCallBack)
        }
    }

    private fun onClickCallBack(data:String){
        if(::clickSubmitListener.isInitialized){
            clickSubmitListener(data)
        }
    }

    class OnTvGlobalLayoutListener(var view: TextView) : OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            val newText: String = autoSplitText(view) ?: ""
            if (!TextUtils.isEmpty(newText)) {
                view.text = newText
            }
        }

        private fun autoSplitText(tv: TextView): String? {
            val rawText = tv.text.toString() //原始文本
            val tvPaint: Paint = tv.paint //paint，包含字体等信息
            val tvWidth = tv.width - tv.paddingLeft - tv.paddingRight.toFloat() //控件可用宽度

            //将原始文本按行拆分
            val rawTextLines =
                rawText.replace("\r".toRegex(), "").split("\n".toRegex()).toTypedArray()
            val sbNewText = StringBuilder()
            for (rawTextLine in rawTextLines) {
                if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                    //如果整行宽度在控件可用宽度之内，就不处理了
                    sbNewText.append(rawTextLine)
                } else {
                    //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                    var lineWidth = 0f
                    var cnt = 0
                    while (cnt != rawTextLine.length) {
                        val ch = rawTextLine[cnt]
                        lineWidth += tvPaint.measureText(ch.toString())
                        if (lineWidth <= tvWidth) {
                            sbNewText.append(ch)
                        } else {
                            sbNewText.append("\n")
                            lineWidth = 0f
                            --cnt
                        }
                        ++cnt
                    }
                }
                sbNewText.append("\n")
            }

            //把结尾多余的\n去掉
            if (!rawText.endsWith("\n")) {
                sbNewText.deleteCharAt(sbNewText.length - 1)
            }
            return sbNewText.toString()
        }

    }


}