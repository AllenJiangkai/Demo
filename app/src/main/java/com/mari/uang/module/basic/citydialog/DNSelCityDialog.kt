package com.mari.uang.module.basic.citydialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mari.uang.R
import com.mari.uang.util.JsonUtil.getCityList
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.dia_city_view.*

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.widget.citydialog
 * @ClassName:      DNSelCityDialog
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/17 4:40 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/17 4:40 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class DNSelCityDialog(context: Context) : Dialog(context, R.style.TipsDialogTheme),
    View.OnClickListener {

    interface CityCallBack {
        fun callItemSel(
            pBean: MUCityModel?,
            cBean: MUCityModel?,
            aBean: MUCityModel?,
            address: String?
        )
    }

    var cityCallBack: CityCallBack? = null

    private val TYPE_PROVINCE = 0
    private val TYPE_CITY = 1
    private val TYPE_AREA = 2
    private var listType: Int = TYPE_PROVINCE
    private var provinceList: List<MUCityModel>? = null
    private var cityList: ArrayList<MUCityModel>? = null
    private var areaList: ArrayList<MUCityModel>? = null

    private var pBean: MUCityModel? = null
    private var cBean: MUCityModel? = null
    private var aBean: MUCityModel? = null

    private var defSel: String? = null

    private val cityAdapter by lazy { CityAdapter() }
    private var currentList: List<MUCityModel>? = null

    init {

        setContentView(R.layout.dia_city_view)
        tv1.setOnClickListener(this)
        tv2.setOnClickListener(this)
        tv3.setOnClickListener(this)
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        defSel = getContext().getString(R.string.please_select)


        rcv.layoutManager = LinearLayoutManager(context)
        rcv.adapter = cityAdapter
        cityAdapter.setNewData(getCityList(context))
        provinceList = cityAdapter.data
        cityAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                var currentBen = cityAdapter.data[position] as MUCityModel

                when (listType) {
                    TYPE_PROVINCE -> {
                        listType = TYPE_CITY
                        pBean = currentBen
                        cityList = currentBen.listData
                        tv1.setText(currentBen.name)
                        tv2.setText(defSel)
                        tv2.isEnabled = true
                        tv3.text = ""
                        tv3.isEnabled = false
                    }
                    TYPE_CITY -> {
                        listType = TYPE_AREA
                        cBean = currentBen
                        areaList = currentBen.listData
                        tv2.setText(currentBen.name)
                        tv2.isEnabled = true
                        tv3.setText(defSel)
                        tv3.isEnabled = true
                    }
                    TYPE_AREA -> {
                        listType = TYPE_PROVINCE
                        aBean = currentBen
                        tv3.setText(currentBen.name)
                        tv2.isEnabled = true
                        tv3.isEnabled = true
                    }
                }
                selTabTextColor(listType)
                if (listType == TYPE_PROVINCE || currentBen == null || currentBen.listData == null || currentBen!!.listData!!.isEmpty()) {
                    if (cityCallBack != null) {
                        val sb = StringBuilder()
                        if (pBean != null) {
                            sb.append(pBean!!.name)
                        }
                        if (cBean != null) {
                            sb.append("-" + cBean!!.name)
                        }
                        if (aBean != null) {
                            sb.append("-" + aBean!!.name)
                        }
                        cityCallBack?.callItemSel(pBean, cBean, aBean, sb.toString())
                    }
                    dismiss()
                    return@OnItemClickListener

                }

                if (listType != TYPE_PROVINCE) {
                    cityAdapter.setNewData(currentBen.listData ?: ArrayList())
                }
            }

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            tv1.id -> {
                listType = TYPE_PROVINCE
                currentList = provinceList
                cityAdapter.selBean = pBean
            }
            tv2.id -> {
                listType = TYPE_CITY
                currentList = cityList
                cityAdapter.selBean = cBean
            }
            tv3.id -> {
                listType = TYPE_AREA
                currentList = areaList
                cityAdapter.selBean = aBean
            }
        }

        selTabTextColor(listType)
        cityAdapter.setNewData(currentList)
    }


    fun selTabTextColor(checkId: Int) {
        when (checkId) {
            TYPE_PROVINCE -> {
                tv1.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_999999
                    )
                )
                tv2.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_999999
                    )
                )
                tv3.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_999999
                    )
                )
            }
            TYPE_CITY -> {
                tv1.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_999999
                    )
                )
                tv2.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.main_color
                    )
                )
                tv3.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_999999
                    )
                )
            }
            TYPE_AREA -> {
                tv1.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_999999
                    )
                )
                tv2.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_999999
                    )
                )
                tv3.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.main_color
                    )
                )
            }
        }
    }
}