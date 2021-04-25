package com.mari.uang.module.product

import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bigkoo.pickerview.OptionsPickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.extentions.createViewModel
import com.coupang.common.extentions.gone
import com.coupang.common.extentions.visible
import com.coupang.common.utils.setHtmlText
import com.coupang.common.utils.setStatusBarTextColor
import com.coupang.common.utils.strings
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig.MODULE_ID_KEY
import com.mari.uang.config.ConstantConfig.ORDER_NO_KEY
import com.mari.uang.config.ConstantConfig.POSITION_ID_KEY
import com.mari.uang.config.ConstantConfig.POSITION_KEY
import com.mari.uang.config.ConstantConfig.PRODUCT_ID_KEY
import com.mari.uang.config.ConstantConfig.STATUS_KEY
import com.mari.uang.config.ConstantConfig.TASK_TYPE_KEY
import com.mari.uang.config.ConstantConfig.TITLE_KEY
import com.mari.uang.module.auth.AuthFaceActivity
import com.mari.uang.module.basic.AuthBasicInfoActivity
import com.mari.uang.module.contact.ContactActivity
import com.mari.uang.module.product.dto.AgreementInfo
import com.mari.uang.module.product.dto.ProductDetailsInfo
import com.mari.uang.module.product.dto.VerifyInfo
import com.mari.uang.util.RouterUtil
import com.mari.uang.util.TypefaceUtil
import kotlinx.android.synthetic.main.fragment_other.rcv_content
import kotlinx.android.synthetic.main.fragment_other.swipe_refresh
import kotlinx.android.synthetic.main.fragment_productr.*
import okhttp3.Route


class ProductActivity : BaseSimpleActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by lazy { createViewModel(ProductModel::class.java) }
    private val adapter by lazy { ProductAdapter(R.layout.item_product_verify) }
    private var productDetailsInfo: ProductDetailsInfo? = null

    private var productId: String? = null
    private var moduleId: String? = null
    private var positionId: String? = null
    private var position: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_productr
    }

    override fun initView() {
        initIntent()
        title_bar.onClickBackListener {
            title_bar.setTitle(strings(R.string.app_name))
            finish()
        }
        initRecyclerView()

        TypefaceUtil.setTextTypeface(tv_amount)
        TypefaceUtil.setTextTypeface(tv_cycle)
        TypefaceUtil.setTextTypeface(tv_tag1_text)
        TypefaceUtil.setTextTypeface(tv_tag2_text)

        swipe_refresh.setOnRefreshListener(this)
        setHtmlText(tv_tips, getString(R.string.product_tips))

        bt_submit.setOnClickListener {
            productDetailsInfo?.apply {
                if (!swipe_refresh.isRefreshing) {
                    if (!checkNetStep() && check_box.isChecked) {
                        viewModel.sendProduct(this)
                    }
                }
            }
        }

        tv_amount.setOnClickListener {
            showSelectOptionDialog (it, productDetailsInfo?.productDetail?.amountArr)
        }
        tv_cycle.setOnClickListener {
            showSelectOptionDialog (it, productDetailsInfo?.productDetail?.termArr)
        }


    }


    fun initIntent() {
        productId = intent.getStringExtra(PRODUCT_ID_KEY)
        moduleId = intent.getStringExtra(MODULE_ID_KEY)
        positionId = intent.getStringExtra(POSITION_ID_KEY)
        position = intent.getStringExtra(POSITION_KEY)
    }


    override fun registerObserver() {
        registerLiveDataCommonObserver(viewModel)
        viewModel.networkError.observe(this, Observer {
            swipe_refresh.isRefreshing = false
        })
        viewModel.productUrl.observe(this, Observer {
            RouterUtil.goWebActivity(this,it,"")
        })
        viewModel.productDetailsInfo.observe(this, Observer {
            swipe_refresh.isRefreshing = false
            productDetailsInfo = it
            adapter.setNewData(it.verify)
            initHeadUi(it)
            initAgreement(it.agreement)
        })

        viewModel.sendEvent.observe(this, Observer {
            RouterUtil.goWebActivity(this,it.url?:"","")
        })
    }

    private fun initHeadUi(info: ProductDetailsInfo?) {
        info?.apply {
            tv_amount_desc.text = info.productDetail?.amountDesc
            tv_amount.text = info.productDetail?.amount
            tv_cycle_desc.text = info.productDetail?.termDesc
            tv_cycle.text = info.productDetail?.term
            tv_tag1_title.text = info.productDetail?.columnText?.tag1?.title
            tv_tag1_text.text = info.productDetail?.columnText?.tag1?.text
            tv_tag2_title.text = info.productDetail?.columnText?.tag2?.title
            tv_tag2_text.text = info.productDetail?.columnText?.tag2?.text
            title_bar.setTitle(info.productDetail?.productName ?: "")
        }
    }

    override fun initData() {

    }


    private fun initAgreement(agreementList: ArrayList<AgreementInfo>?) {
        if (agreementList == null || agreementList.size == 0) {
            ll_check.gone()
            check_box.gone()
            check_box.isChecked = true
            tv_agreement.gone()
            tv_ksp.gone()
        } else {
            tv_agreement.visible()
            tv_ksp.visible()
            val ssb = SpannableStringBuilder("")
            for (i in agreementList.indices) {
                addSpannableString(ssb, agreementList[i])
                ssb.append("  ")
            }
            tv_ksp.setText(ssb, TextView.BufferType.SPANNABLE)
            tv_ksp.movementMethod = LinkMovementMethod.getInstance()
            tv_ksp.highlightColor = ContextCompat.getColor(this, R.color.transparent)
        }
    }

    private fun initRecyclerView() {
        rcv_content.adapter = adapter
        rcv_content.layoutManager = LinearLayoutManager(this)

        adapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                if (swipe_refresh.isRefreshing) {
                    return@OnItemClickListener
                }
                productDetailsInfo?.apply {
                    var bean = adapter.data[position] as VerifyInfo
                    if (bean.status != null && bean.status.equals("1") && productDetailsInfo?.nextStep != null) {
                        goAuthActivity(bean.type, bean.taskType, bean.title, bean.status, bean.url)
                    } else {
                        if (!checkNetStep()) {
                            goAuthActivity(
                                bean.type,
                                bean.taskType,
                                bean.title,
                                bean.status,
                                bean.url
                            )
                        }
                    }
                }
            }
    }

    private fun goAuthActivity(
        type: String?,
        taskType: String?,
        title: String?,
        status: String?,
        url: String?
    ) {
        if (TextUtils.isEmpty(type)) return

        if (type == "0") {
            when (taskType) {
                "public" -> {
                    startActivity(Intent(this, AuthFaceActivity::class.java).apply {
                        putExtra(PRODUCT_ID_KEY, productId)
                        putExtra(TITLE_KEY, title)
                        putExtra(STATUS_KEY, status)
                        putExtra(ORDER_NO_KEY, productDetailsInfo?.productDetail?.orderNo)
                    })
                }
                "ext" -> {
                    startActivity(Intent(this, ContactActivity::class.java).apply {
                        putExtra(PRODUCT_ID_KEY, productId)
                        putExtra(TITLE_KEY, title)
                        putExtra(STATUS_KEY, status)
                    })
                }
                "bank", "job", "personal" -> {

                    startActivity(Intent(this, AuthBasicInfoActivity::class.java).apply {
                        putExtra(PRODUCT_ID_KEY, productId)
                        putExtra(TITLE_KEY, title)
                        putExtra(STATUS_KEY, status)
                        putExtra(TASK_TYPE_KEY, taskType)
                    })

                }
            }
        } else if (type == "1") {
//            MUNaviUtils.getInstance().jumpWebFrag(url, title)
        }
    }


    private fun checkNetStep(): Boolean {
        return if (productDetailsInfo?.nextStep != null) {
            if (productDetailsInfo?.nextStep?.type == 0) {
                goAuthActivity(
                    productDetailsInfo?.nextStep?.type.toString(),
                    productDetailsInfo?.nextStep?.step,
                    productDetailsInfo?.nextStep?.title,
                    "",
                    productDetailsInfo?.nextStep?.url
                )
            } else if (productDetailsInfo?.nextStep?.type == 1) {
                RouterUtil.goWebActivity(this ,productDetailsInfo?.nextStep?.url?:"",productDetailsInfo?.nextStep?.title?:"")
            }
            true
        } else {
            //跳认证
            false
        }
    }

    override fun onRefresh() {
        viewModel.requestProductDetail(productId, moduleId, positionId, position)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
        onRefresh()
    }

    private fun addSpannableString(ssb: SpannableStringBuilder, bean: AgreementInfo) {
        val textStartIndex = ssb.length
        ssb.append(bean.title)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                viewModel.requestProductAgreement(bean)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = resources.getColor(R.color.main_color)
                ds.isUnderlineText = false
            }
        }
        ssb.setSpan(clickableSpan, textStartIndex, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun showSelectOptionDialog(view: View, list: ArrayList<String>?) {
        OptionsPickerView.Builder(this) { options1, options2, options3, v ->
            when (view.id) {
                R.id.tv_cycle ->{
                   productDetailsInfo?. productDetail?.term= list?.get(options1)
                    initHeadUi(productDetailsInfo)
                }
                R.id.tv_amount -> {
                    productDetailsInfo?. productDetail?.amount= list?.get(options1)
                    initHeadUi(productDetailsInfo)
                }
            }

        }.setSubCalSize(16)
            .setSubmitColor(ContextCompat.getColor(this, R.color.main_color))
            .setCancelColor(ContextCompat.getColor(this, R.color.color_999999))
            .setTextColorCenter(ContextCompat.getColor(this, R.color.color_666666))
            .setTextColorOut(ContextCompat.getColor(this, R.color.color_999999))
            .setDividerColor(ContextCompat.getColor(this, R.color.color_f4f5f6))
            .setTitleBgColor(ContextCompat.getColor(this, R.color.white))
            .setContentTextSize(20)
            .setDividerColor(Color.BLACK)
            .setCancelText(this.getString(R.string.dialog_per_left))
            .setSubmitText(this.getString(R.string.dialog_confirm))
            .setOutSideCancelable(true)
            .build().apply {
                setPicker(list)
                show()
            }


    }

}