package com.de.danaemas.module.home.widget

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.coupang.common.user.UserManager
import com.de.danaemas.R
import com.de.danaemas.module.home.dto.RepayNoticeInfo
import com.de.danaemas.module.login.LoginActivity
import com.de.danaemas.util.RouterUtil
import kotlinx.android.synthetic.main.widget_repay_notice_view.view.*
import java.util.regex.Pattern

class RepayNoticeView : RelativeLayout {

    private lateinit var clickListener: () -> Unit

    fun onClickButtonListener(callback: () -> Unit) {
        this.clickListener = callback
    }


    private lateinit var inflateView: View

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        inflateView = View.inflate(
            context,
            R.layout.widget_repay_notice_view,
            this@RepayNoticeView
        )

//        setOnClickListener {
//            if (::clickListener.isInitialized) {
//                clickListener()
//            }
//        }
    }

    fun initData(bean: ArrayList<RepayNoticeInfo>){
        val flipperCreator = object : NoticeAdapter(context) {

            override fun getCount(): Int {
                return bean.size
            }

            override fun initView(context: Context?, position: Int): View? {
                val entry: RepayNoticeInfo = bean[position]
                val mItemView = inflate(mContext, R.layout.layout_repay_notice_flipper, null)
                val tv_title = mItemView.findViewById<TextView>(R.id.tv_title)
                val tv_message = mItemView.findViewById<TextView>(R.id.tvMessage)
                if (!TextUtils.isEmpty(entry.title)) {
                    tv_title.text = Html.fromHtml(entry.title)
                }
                if (isColor(entry.title)) {
                    val bg = tv_title.background.mutate() as GradientDrawable
                    bg.setTint(Color.parseColor(entry.title))
                }
                if (!TextUtils.isEmpty(entry.message)) {
                    tv_message.text = Html.fromHtml(entry.message)
                }
                mItemView.setOnClickListener { view: View? ->
                    if(!UserManager.isLogin()){
                        mContext?.startActivity(Intent(mContext, LoginActivity::class.java))
                    }else {
                        RouterUtil.router(mContext, bean[position].url)
                    }
                }
                return mItemView

            }
        }
        flipperCreator.setViewFlipper(flipper)
        flipperCreator.setAnimation(R.anim.noticle_in, R.anim.noticle_out)
        flipperCreator.setFlipInterval(5 * 1000)
        flipperCreator.create()
    }

    fun isColor(color: String?): Boolean {
        if (TextUtils.isEmpty(color)) return false
        val p = Pattern.compile("#[0-9a-fA-F]{6,8}$")
        val m = p.matcher(color)
        return m.matches()
    }

}