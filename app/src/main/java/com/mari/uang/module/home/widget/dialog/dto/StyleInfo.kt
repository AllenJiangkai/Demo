package com.mari.uang.module.home.widget.dialog.dto

import com.coupang.common.network.DTO
import com.mari.uang.module.home.dto.DialogButtonInfo
import com.mari.uang.module.home.dto.DialogTextInfo


open class BaseDialogInfo :DTO{
    var type: String? = null
    var bottomMargin = 0
    var topMargin = 0
}

class MessageInfo : BaseDialogInfo() {
    var msg: DialogTextInfo? = null
}
class Info : BaseDialogInfo() {
    var title: DialogTextInfo? = null
    var info: DialogTextInfo? = null
}
class  ImgInfo : BaseDialogInfo() {
    var img: String? = null
}
class  SingleBt : BaseDialogInfo() {
   var marginHorizontal = 0
   var height = 0
   var btn: DialogButtonInfo? = null
}
class  DoubleBt : BaseDialogInfo() {
    var height = 0
  var list: List<DialogButtonInfo>? = null
}