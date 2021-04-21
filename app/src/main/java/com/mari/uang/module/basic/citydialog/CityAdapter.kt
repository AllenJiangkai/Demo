package com.mari.uang.module.basic.citydialog

import com.mari.uang.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.coupang.common.utils.colors

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.widget.citydialog
 * @ClassName:      CityAdapter
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/17 4:56 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/17 4:56 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class CityAdapter(layoutResId: Int = R.layout.item_city) :
    BaseQuickAdapter<MUCityModel, BaseViewHolder>(layoutResId) {

    var selBean : MUCityModel? = null

    override fun convert(helper: BaseViewHolder, item: MUCityModel) {
        helper.setText(R.id.tvName,item.name)
        if (selBean != null && selBean?.code === item.code){
            helper.setTextColor(R.id.tvName,colors(R.color.main_color))
        }else{
            helper.setTextColor(R.id.tvName, colors(R.color.color_999999))
        }
    }
}