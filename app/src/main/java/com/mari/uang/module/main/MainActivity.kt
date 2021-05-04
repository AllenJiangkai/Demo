package com.mari.uang.module.main

import android.content.Intent
import android.text.TextUtils
import androidx.viewpager.widget.ViewPager
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.network.ParameterTool
import com.coupang.common.user.UserManager.isLogin
import com.coupang.common.utils.setStatusBarTextColor
import com.coupang.common.utils.spf.SpConfig
import com.coupang.common.utils.strings
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mari.uang.AppApi
import com.mari.uang.R
import com.mari.uang.config.AFAction
import com.mari.uang.config.ConstantConfig
import com.mari.uang.module.home.HomeFragment
import com.mari.uang.module.login.LoginActivity
import com.mari.uang.module.profile.ProfileFragment
import com.mari.uang.util.EventBusAction
import com.mari.uang.util.EventUtil
import com.mari.uang.util.RouterUtil
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseSimpleActivity() {

    private val navigator by lazy { getBottomIndicatorNavigator() }
    private val tabContentList: List<TabContent> by lazy {
        listOf(
            TabContent(
                strings(R.string.first_fragment_label),
                R.drawable.selector_tab_icon_first,
                HomeFragment.newInstance(),
                false
            ), TabContent(
                strings(R.string.second_fragment_label),
                R.drawable.selector_tab_icon_second,
                ProfileFragment.newInstance(),
                false
            )
        )
    }


    private fun initIntent() {
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var index = intent?.getIntExtra("index", 0) ?: 0
        switchTab(index)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, false)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        EventUtil.event(this, AFAction.APP_HOME_PAGE)
        EventBus.getDefault().register(this)
        initIntent()
//
//        tv_test.setOnClickListener {
//            viewModel.login()
//        }

        initTabView()
        subscribePush()
        getPushAction(SpConfig.pushData)
    }

    override fun initData() {

    }


    private fun switchTab(tabIndex: Int) {
        if (tabContentList.isEmpty()) {
            return
        }
        if (tabIndex >= 0 && tabIndex < tabContentList.size) {
            tabContainerView.setCurrentItem(tabIndex, true)
        }

    }

    override fun registerObserver() {

//        registerLiveDataCommonObserver(viewModel)

    }

    private fun initTabView() {
        tabContainerView.supportScroll = false
        tabContainerView.offscreenPageLimit = 2
        tabContainerView.adapter = TabContentPagerAdapter(supportFragmentManager, tabContentList)
        bottomIndicator.navigator = navigator
        ViewPagerHelper.bind(bottomIndicator, tabContainerView)
        tabContainerView.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                setStatusBarTextColor(window, tabContentList[position].isLightStatusBar)
            }
        })
    }

    private fun getBottomIndicatorNavigator() = CommonNavigator(this).also { navigator ->
        navigator.isAdjustMode = true
        navigator.adapter = TabIndicatorNavigatorAdapter(tabContentList).also { indicatorAdapter ->
            indicatorAdapter.tabIndicatorItemClickCallback =
                object : TabIndicatorItemClickCallback {
                    override fun clickIndicatorItem(index: Int) {
                        if (index == 1 && !isLogin()) {
                            EventUtil.event(this@MainActivity, AFAction.APP_CLICK_TAB_MINE)
                            goLoginActivity()
                            return
                        }
                        this@MainActivity.tabContainerView.setCurrentItem(index, false)
                    }
                }
        }

    }

    private fun goLoginActivity() {
        val intent = Intent(
            this@MainActivity,
            LoginActivity::class.java
        )
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAppBusEvent(appBusBean: EventBusAction<Any>){
        if (TextUtils.isEmpty(appBusBean.action))
            return
        when(appBusBean.action){
            ConstantConfig.PUSH_JUMP_URL_KEY -> {
                getPushAction(appBusBean.data as String)
            }
        }
    }

    private fun subscribePush() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result!!.token
                if (token == null || token.length == 0) return@OnCompleteListener
                var map : MutableMap<String,Any> = HashMap()
                map["fcm_token"] = token
                AppApi.api.fcmToken(ParameterTool.toRequestBody(map))
            })
    }

    private fun getPushAction(dataUrl : String){
        if (!TextUtils.isEmpty(dataUrl) && (dataUrl == RouterUtil.APP_SCHEME || dataUrl == "http")){
            SpConfig.pushData = ""
            RouterUtil.router(this, dataUrl)
        }
    }
}
