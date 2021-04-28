package com.mari.uang.module.basic.dto

import com.coupang.common.network.DTO
import com.mari.uang.module.contact.dto.NameTypeInfo

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.module.basic.dto
 * @ClassName:      BasicItemInfo
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/18 3:18 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/18 3:18 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class BasicItemInfo : DTO{

    var id: String? = null
    var title: String? = null
    var subtitle: String? = null
    var code: String? = null
    var cate: String? = null
    var optional: String? = null
    var status: String? = null
    var statusName: String? = null
    var enable: Boolean? = null
    var value: String? = null
    var dateSelect: String? = null
    var note: ArrayList<NameTypeInfo>? = null
    var inputType: String? = null

}

class BasicItemInfoList : DTO{
    var items:ArrayList<BasicItemInfo>?=null
}