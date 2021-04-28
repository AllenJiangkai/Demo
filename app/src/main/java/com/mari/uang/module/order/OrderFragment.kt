package com.mari.uang.module.order

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.base.LazyLoadFragment
import com.coupang.common.extentions.createViewModel
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig.MODULE_ID_KEY
import com.mari.uang.config.ConstantConfig.MODULE_ORDER
import com.mari.uang.config.ConstantConfig.POSITION_ID_KEY
import com.mari.uang.config.ConstantConfig.POSITION_KEY
import com.mari.uang.config.ConstantConfig.PRODUCT_ID_KEY
import com.mari.uang.module.main.MainActivity
import com.mari.uang.module.product.ProductActivity
import com.mari.uang.util.RouterUtil
import com.mari.uang.widget.OrderEmptyView
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : LazyLoadFragment() {

    private val viewModel by lazy { createViewModel(OrderModel::class.java) }
    private val adapter by lazy { OrderListAdapter(R.layout.item_order) }
    private var statusId: Int = 0

    companion object {
        fun newInstance(statusId: Int): OrderFragment {
            val fragment = OrderFragment()
            val bundle = Bundle()
            bundle.putInt("statusId", statusId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusId = arguments?.getInt("statusId", 0) ?: 0
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_order
    }

    override fun initUIViews() {
        registerObserver()
        initRecyclerView()
        swipe_refresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun initRecyclerView() {
        rcv_content.adapter = adapter
        adapter.setPreLoadNumber(2)

        rcv_content.layoutManager = LinearLayoutManager(activity)
        //点击事件
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.rl_main -> {
                    var bean = adapter.data[position] as OrderInfo

                    if (!TextUtils.isEmpty(bean.loanDetailUrl)) {
                        RouterUtil.goWebActivity(context, bean.loanDetailUrl?:"","" )
                    } else {
                        startActivity(Intent(activity, ProductActivity::class.java).apply {
                            putExtra(MODULE_ID_KEY, MODULE_ORDER)
                            putExtra(POSITION_ID_KEY, statusId.toString())
                            putExtra(POSITION_KEY, position.toString())
                            putExtra(PRODUCT_ID_KEY, bean.productId)
                        })
                    }
                }
            }
        }
    }

    override fun loadData() {
        viewModel.orderList(statusId)
    }

    private fun registerObserver() {
        (activity as? BaseSimpleActivity)?.registerLiveDataCommonObserver(viewModel)
        viewModel.orderList.observe(this, Observer {
            adapter.setNewData(it.list)
            swipe_refresh.isRefreshing = false

            if (it.list.isNullOrEmpty()) {
                activity?.let {
                    val empty = OrderEmptyView(it)
                    empty.onClickButtonListener {
                        startActivity(Intent(activity, MainActivity::class.java))
                    }
                    adapter.emptyView = empty
                }
            }
        })
        viewModel.networkError.observe(this, Observer {
            swipe_refresh.isRefreshing = false
        })
    }

    override fun isNeedOnPauseReloadData(): Boolean {
        return true
    }

    override fun isNeedReloadData(): Boolean {
        return true
    }

}