package com.mari.uang.module.profile

import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.base.LazyLoadFragment
import com.coupang.common.extentions.createViewModel
import com.coupang.common.extentions.gone
import com.coupang.common.user.UserManager
import com.coupang.common.utils.spf.SpConfig.orderType1
import com.coupang.common.utils.spf.SpConfig.orderType2
import com.coupang.common.utils.spf.SpConfig.redDotData
import com.mari.uang.R
import com.mari.uang.module.order.OrderActivity
import com.mari.uang.module.order.OrderActivity.Companion.BELUM_SELESAI
import com.mari.uang.module.order.OrderActivity.Companion.DANA_CAIR
import com.mari.uang.module.order.OrderActivity.Companion.SUDAH_LUNAS
import com.mari.uang.util.RouterUtil
import kotlinx.android.synthetic.main.fragment_other.*


class ProfileFragment : LazyLoadFragment(), SwipeRefreshLayout.OnRefreshListener {
    private val viewModel by lazy { createViewModel(ProfileModel::class.java) }
    private val adapter by lazy { ProfileItemAdapter(R.layout.item_profile_item) }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_other
    }

    override fun initUIViews() {
        registerObserver()
        initRecyclerView()
        rl_order_one.setOnClickListener {
            red_view_one.gone()
            (red_view_one.tag as? String)?.apply {
                orderType1= this
            }
            goOrderActivity(BELUM_SELESAI)
        }
        rl_order_two.setOnClickListener {
            red_view_two.gone()
            (red_view_two.tag as? String)?.apply {
                orderType2= this
            }
            goOrderActivity(DANA_CAIR)
        }
        rl_order_three.setOnClickListener {
            goOrderActivity(SUDAH_LUNAS)
        }
        swipe_refresh.setOnRefreshListener(this)
        tv_phone.text=encryptionPhone(UserManager.username)
    }

    override fun isNeedReloadData(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        tv_phone.text=encryptionPhone(UserManager.username)
    }
    fun encryptionPhone(phone: String?): String? {
        if (TextUtils.isEmpty(phone)) return ""
        val sb = StringBuilder(phone!!)
        return sb.replace(3, 7, "****").toString()
    }

    override fun loadData() {
        viewModel.requestItemList()
        viewModel.requestRedData()
    }

    private fun registerObserver() {
        (activity as? BaseSimpleActivity)?.registerLiveDataCommonObserver(viewModel)
        viewModel.profileItemInfo.observe(this, Observer {
            adapter.setNewData(it.extendLists)
            if ("1" == it.ifRedPoint) {
                if (orderType1.isNotEmpty()&&orderType1 != it.redPointId) {
                    refreshRedView(red_view_one, it.redPointId)
                }
            }
            swipe_refresh.isRefreshing = false
        })
        viewModel.profileRedInfo.observe(this, Observer {
            redDotData = JSONObject.toJSONString(it)
            initRedDot()
            swipe_refresh.isRefreshing = false
        })
        viewModel.networkError.observe(this, Observer {
            swipe_refresh.isRefreshing = false
        })
    }

    private fun refreshFragment() {
        loadData()
    }

    private fun goOrderActivity(type :String){
        startActivity(Intent(activity,OrderActivity::class.java).putExtra("tab",type))
    }

    private fun initRecyclerView() {
        rcv_content.adapter = adapter
        rcv_content.layoutManager = LinearLayoutManager(activity)

        adapter.setOnItemClickListener { adapter, view, position ->
           RouterUtil.router(context,(adapter.data[position] as ItemInfo ).linkUrl,false)
        }
    }


    override fun onRefresh() {
        refreshFragment()
    }


    private fun refreshRedView(textView: View, tag: String?) {
        textView.visibility = View.VISIBLE
        textView.tag = tag
    }


    private fun initRedDot() {
        if (!TextUtils.isEmpty(redDotData)) {
            val redDotList: List<ProfileRedInfo> = JSON.parseArray(
                redDotData,
                ProfileRedInfo::class.java
            )
            if (redDotList.isEmpty()) return
            for (redDotBean in redDotList) {
                if ("repayment" == redDotBean.type) {
                    if (orderType2 != redDotBean.time) {
                        refreshRedView(red_view_two, redDotBean.time)
                    }
                }
            }
        }
    }

}