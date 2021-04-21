package com.mari.uang.module.home.widget

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.coupang.common.user.UserManager
import com.mari.uang.R
import com.mari.uang.module.home.dto.NoticeInfo
import com.mari.uang.module.login.LoginActivity
import com.mari.uang.util.RouterUtil
import kotlinx.android.synthetic.main.widget_notice_view.view.*

class NoticeView : RelativeLayout {

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
            R.layout.widget_notice_view,
            this@NoticeView
        )

//        setOnClickListener {
//            if (::clickListener.isInitialized) {
//                clickListener()
//            }
//        }
    }

    fun initData(bean: ArrayList<NoticeInfo>){
        val flipperCreator: NoticeAdapter = object : NoticeAdapter(context) {
            override fun getCount(): Int {
              return bean.size
            }

            override fun initView(context: Context?, position: Int): View? {
                val mItemView = inflate(mContext, R.layout.layout_notice_flipper, null)
                val tvNotice = mItemView.findViewById<TextView>(R.id.tvContent)
                if (!TextUtils.isEmpty(bean[position].message)) {
                    tvNotice.setText(bean[position].message)
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
        flipperCreator.setFlipInterval(3000)
        flipperCreator.create()
    }
}