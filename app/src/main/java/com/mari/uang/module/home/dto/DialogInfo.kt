package com.mari.uang.module.home.dto

import com.alibaba.fastjson.JSONObject
import com.coupang.common.network.DTO

class DialogInfo :DTO{
    var title: DialogTextInfo? = null
    var showClose = 0
    var cancelable = 0
    var cancelableOutside = 0

    var bottomBtnType  = 0

    var list: List<JSONObject>? = null

    var singleBtn: DialogButtonInfo? = null

    var multipleBtnList: List<DialogButtonInfo>? = null
}

class DialogTextInfo:DTO{
    var text: String? = null
    var size = 0
    var color: String? = null
}
class DialogButtonInfo:DTO{
    var text: String? = null
    var size = 0
    var color: String? = null
    var bgColor: String? = null
    var url: String? = null
    var pid: String? = null
    var type = 0
    var close = 0
}

