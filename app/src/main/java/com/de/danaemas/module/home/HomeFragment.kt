package com.de.danaemas.module.home

import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.base.LazyLoadFragment
import com.coupang.common.extentions.createViewModel
import com.coupang.common.user.UserManager.isLogin
import com.de.danaemas.R
import com.de.danaemas.config.AFAction
import com.de.danaemas.config.ConstantConfig.MODULE_DIALOG
import com.de.danaemas.config.ConstantConfig.MODULE_HOME
import com.de.danaemas.config.ConstantConfig.POSITION_LARGE
import com.de.danaemas.config.ConstantConfig.PRODUCT_ID_KEY
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_BIG_CARD
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_PRODUCT_CARD
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_SMALL_CARD
import com.de.danaemas.module.home.dto.CardInfo
import com.de.danaemas.module.home.dto.DialogInfo
import com.de.danaemas.module.home.dto.ProductInfo
import com.de.danaemas.module.home.widget.dialog.ProductDialog
import com.de.danaemas.module.login.LoginActivity
import com.de.danaemas.module.product.ProductActivity
import com.de.danaemas.util.EventUtil
import com.de.danaemas.util.RouterUtil
import kotlinx.android.synthetic.main.fragment_recommend.*


class HomeFragment : LazyLoadFragment() {

    private val viewModel by lazy { createViewModel(HomeModel::class.java) }
    private val adapter by lazy { HomeAdapter() }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recommend
    }

    override fun initUIViews() {
        swipe_refresh.setOnRefreshListener {
            loadData()
        }

        registerObserver()
        initRecyclerView()
    }

    override fun loadData() {
        viewModel.requestHomeList()
    }

    private fun initRecyclerView() {
        rcv_content.adapter = adapter
        adapter.setPreLoadNumber(2)
        rcv_content.layoutManager = LinearLayoutManager(activity)
        //点击事件
//        adapter.setOnItemChildClickListener { adapter, view, position ->
//            when (view.id) {
//                R.id.rl_status -> {
//                }
//            }
//        }

        adapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.data[position] as HomeAdapterItem<*>
            if (!isLogin()) {
                goLoginActivity()
                EventUtil.event(context, AFAction.APP_CLICK_PRODUCT)
                return@setOnItemClickListener
            }

            when (item.itemType) {
                TYPE_RECOMMEND_BIG_CARD, TYPE_RECOMMEND_SMALL_CARD -> {
                    var bean = item.data as CardInfo
                    viewModel.requestProduct(MODULE_HOME, POSITION_LARGE, null, bean.id ?: "")
                }
                TYPE_RECOMMEND_PRODUCT_CARD -> {
                    var bean = item.data as ProductInfo
                    viewModel.requestProduct(MODULE_HOME, POSITION_LARGE, null, bean.id ?: "")
                }
            }
        }
    }

    private fun goLoginActivity() {
        startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun registerObserver() {
        (activity as? BaseSimpleActivity)?.registerLiveDataCommonObserver(viewModel)
        viewModel.homeList.observe(this, Observer {
            adapter.setNewData(it)
            swipe_refresh.isRefreshing = false
        })
        viewModel.homeIcon.observe(this, Observer {
            swipe_refresh.isRefreshing = false
            adapter.iconUrl=it.iconUrl
            adapter.linkUrl=it.linkUrl
//            home_title.initImage(it.iconUrl)
//
//            home_title.onClickRightListener {
//                RouterUtil.router(context, it.linkUrl, false)
//            }
        })
        viewModel.networkError.observe(this, Observer {
            swipe_refresh.isRefreshing = false
        })
        viewModel.dialogInfo.observe(this, Observer {

            var bean = JSON.parseObject(it.dialog, DialogInfo::class.java)
            if (bean == null) {
                if (!TextUtils.isEmpty(it.url)) {
                    RouterUtil.router(context, it.url)
                } else {
                    var intent = Intent(context, ProductActivity::class.java)
                    intent.putExtra(PRODUCT_ID_KEY, it.productId)
                    startActivity(intent)
                }
            } else {
                context?.let { it1 ->
                    ProductDialog(it1).setDialogBean(bean).initUi().onClickSubmitListener {
                        viewModel.requestProduct(MODULE_DIALOG, POSITION_LARGE, null, it)
                    }.show()
                }
            }
        })

    }

}