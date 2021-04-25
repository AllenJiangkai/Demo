package com.mari.uang.module.home.widget

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mari.uang.MyApplication
import com.mari.uang.R
import com.mari.uang.module.home.dto.BannerInfo
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.coupang.common.utils.GlideLoadUtils.loadImage
import com.mari.uang.util.RouterUtil
import kotlinx.android.synthetic.main.widget_banner_view.view.*
import kotlin.math.abs

class HomeBannerView : RelativeLayout {

    private lateinit var clickListener: (position: Int, bean: BannerInfo) -> Unit

    fun onItemClickListener(callback: (position: Int, bean: BannerInfo) -> Unit) {
        this.clickListener = callback
    }

    private lateinit var inflateView: View

    private val delayTime: Long = 3000
    private var currentPosition = 0
    private val OFFSET_MAX = 100f
    private var list: ArrayList<BannerInfo>? = ArrayList()

    private var downX = 0f
    private var downY = 0f

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        inflateView = View.inflate(
            context,
            R.layout.widget_banner_view,
            this@HomeBannerView
        )
        initBannerView()
    }


    fun initRight(iconUrl:String,linkUrl:String){
        loadImage(context,iconUrl,img_right)
        img_right.setOnClickListener {
            RouterUtil.router(context,linkUrl,false)
        }
    }

    fun initBannerViewData(data: ArrayList<BannerInfo>) {
        this.list = data
        var adapter = BannerAdapter(context, list)
        view_page.adapter = adapter
        startAuto()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                pauseAuto()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                startAuto()
                if (canClickBack(ev) && ::clickListener.isInitialized) {
                    clickListener(
                        currentPosition % list!!.size,
                        list!![currentPosition % list!!.size]
                    )
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    var hand = Handler()

    private fun pauseAuto() {

        hand.removeCallbacks(runnable)
    }

    private fun startAuto() {
        hand.postDelayed(runnable, delayTime)
    }

    private fun initBannerView() {


        (view_page.layoutParams).also {
            it.height = (MyApplication.screenWidth / 2.343f).toInt()
            layoutParams = it
        }


        view_page.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                if (!list.isNullOrEmpty()) {
                    loadImage(
                        context,
                        list!![position % list!!.size].imgUrl,
                        object : SimpleTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val ratio: Float = resource.width * 1.0f / (resource.height * 1.0f)
                                view_page.layoutParams.apply {
                                    width = measuredWidth
                                    height = (measuredWidth / ratio).toInt()
                                }
                            }
                        })
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (hand != null) {
            hand.removeCallbacks(runnable)
            hand.removeCallbacksAndMessages(null)
        }
    }

    private fun canClickBack(event: MotionEvent): Boolean {
        return !(abs(downX - event.x) > OFFSET_MAX || abs(downY - event.y) > OFFSET_MAX)
    }


    private val runnable: Runnable by lazy {
        object : Runnable {
            override fun run() {
                currentPosition += 1
                view_page.currentItem = currentPosition
                hand.postDelayed(this, delayTime)
            }
        }
    }

}

class BannerAdapter(var context: Context?, var list: ArrayList<BannerInfo>?) : PagerAdapter() {


    override fun getCount(): Int {
        if (list == null || list!!.isEmpty()) return 0
        return if (list!!.size == 1) 1 else Int.MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.setOnClickListener {

        }

        context?.also {
            loadImage(it, list!![position % list!!.size].imgUrl, imageView)
        }
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}