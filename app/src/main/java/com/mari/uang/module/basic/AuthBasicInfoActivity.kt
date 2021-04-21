package com.mari.uang.module.basic

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig
import com.mari.uang.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_CITY_SELECT
import com.mari.uang.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_ENUM
import com.mari.uang.module.basic.dto.BasicItemInfo
import com.mari.uang.module.contact.dto.NameTypeInfo
import com.mari.uang.module.basic.citydialog.DNSelCityDialog
import com.mari.uang.module.basic.citydialog.MUCityModel
import com.mari.lib_utils.system.KeymapUtils
import com.bigkoo.pickerview.OptionsPickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.extentions.createViewModel
import kotlinx.android.synthetic.main.activity_auth_basic_info.*

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.module.auth
 * @ClassName:      AuthBasicInfoActivity
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/17 1:11 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/17 1:11 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

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

    private fun initRecyclerView() {
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = itemAdapter
        itemAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as BasicInfoAdapterItem<*>

            val infoAuthItemEntity= bean.data as BasicItemInfo
            when (infoAuthItemEntity.cate) {
                INFO_AUTH_ITEM_TYPE_ENUM -> {
                    KeymapUtils.hideInput(this)
                    showPickDialog(getBeanList()?.get(position)?.note , position)
                }
                INFO_AUTH_ITEM_TYPE_CITY_SELECT -> {
                    KeymapUtils.hideInput(this)
                    showCityDialog(position)
                }
            }
        }
    }



    private fun showPickDialog(list: ArrayList<NameTypeInfo>?, position:Int) {
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
            bt_submit.isEnabled=true
        })

        viewModel.saveResult.observe(this, Observer {
            finish()
        })
    }

    override fun initData() {
        viewModel.getData(taskStatus,productId)
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
            viewModel.saveInfoData(getBeanList(),taskStatus,productId,0)
        }
    }


    private fun showCityDialog(position: Int) {
        val selCityDialog= DNSelCityDialog(this)
        selCityDialog.cityCallBack = object: DNSelCityDialog.CityCallBack{
            override fun callItemSel(
                pBean: MUCityModel?,
                cBean: MUCityModel?,
                aBean: MUCityModel?,
                address: String?
            ) {
                getBeanList()?.get(position)?.value = address
                itemAdapter.notifyItemChanged(position)
            }
        }
        selCityDialog.show()
    }
}