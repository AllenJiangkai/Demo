package com.mari.uang.module.basic

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.OptionsPickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.extentions.createViewModel
import com.coupang.common.utils.KeymapUtils
import com.coupang.common.utils.setStatusBarTextColor
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig
import com.mari.uang.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_CITY_SELECT
import com.mari.uang.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_ENUM
import com.mari.uang.module.basic.citydialog.SelCityDialog
import com.mari.uang.module.basic.citydialog.CityModel
import com.mari.uang.module.basic.dto.BasicItemInfo
import com.mari.uang.module.contact.dto.NameTypeInfo
import kotlinx.android.synthetic.main.activity_auth_basic_info.*


class AuthBasicInfoActivity : BaseSimpleActivity()  {

    private var productId: String? = null
    private var taskStatus: String? = null
    private var status: String? = null

    private val itemAdapter by lazy { AuthBasicAdapter() }
    private val viewModel by lazy { createViewModel(BasicInfoModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.activity_auth_basic_info
    }

    override fun initView() {
        title_bar.apply {
            setTitle(context.getString(R.string.title_auth_face))
            onClickBackListener {
                finish()
            }
        }
        initIntent()
        initListener()
        initRecyclerView()
    }
    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
    }

    private fun initRecyclerView() {
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = itemAdapter
        itemAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as BasicInfoAdapterItem<*>

            val infoAuthItemEntity= bean.data as BasicItemInfo
            when (infoAuthItemEntity.cate) {
                INFO_AUTH_ITEM_TYPE_ENUM -> {
                    KeymapUtils.hideInput(this)
                    showPickDialog(getBeanList()?.get(position)?.note, position)
                }
                INFO_AUTH_ITEM_TYPE_CITY_SELECT -> {
                    KeymapUtils.hideInput(this)
                    showCityDialog(position)
                }
            }
        }
    }



    private fun showPickDialog(list: ArrayList<NameTypeInfo>?, position: Int) {
        OptionsPickerView.Builder(this) { options1, options2, options3, v ->
            val bean: NameTypeInfo = list!![options1]
            getBeanList()!![position].value = bean.name
            itemAdapter.notifyItemChanged(position)
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

    override fun registerObserver() {
        registerLiveDataCommonObserver(viewModel)
        viewModel.dataList.observe(this, Observer {
            itemAdapter.setNewData(it)
            bt_submit.isEnabled = true
        })

        viewModel.saveResult.observe(this, Observer {
            finish()
        })
    }

    override fun initData() {
        viewModel.getData(taskStatus, productId)
    }


    private fun initIntent() {
        taskStatus = intent.getStringExtra(ConstantConfig.TASK_TYPE_KEY)
        status = intent.getStringExtra(ConstantConfig.STATUS_KEY)
        productId = intent.getStringExtra(ConstantConfig.PRODUCT_ID_KEY)
        val title: String = intent.getStringExtra(ConstantConfig.TITLE_KEY)
        title_bar.setTitle(title)
    }

    private fun getBeanList( ):ArrayList<BasicItemInfo>{
        var list =ArrayList<BasicItemInfo>()

        itemAdapter.data.forEach {
            var bean =it as  BasicInfoAdapterItem<*>
            list.add(bean.data as BasicItemInfo)
        }
        return list
    }

    private fun initListener() {
        bt_submit.setOnClickListener{
            viewModel.saveInfoData(getBeanList(), taskStatus, productId, pageCreateTime)
        }
    }


    private fun showCityDialog(position: Int) {
        val selCityDialog= SelCityDialog(this)
        selCityDialog.cityCallBack = object: SelCityDialog.CityCallBack{
            override fun callItemSel(
                pBean: CityModel?,
                cBean: CityModel?,
                aBean: CityModel?,
                address: String?
            ) {
                getBeanList()?.get(position)?.value = address
                itemAdapter.notifyItemChanged(position)
            }
        }
        selCityDialog.show()
    }
}