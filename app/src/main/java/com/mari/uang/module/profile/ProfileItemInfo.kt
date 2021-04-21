package com.mari.uang.module.profile

import com.coupang.common.network.DTO

class ProfileItemInfo : DTO {
    var extendLists: ArrayList<ItemInfo>? = null
    var ifRedPoint: String? = null
    var redPointId: String? = null
}

class ItemInfo : DTO {
    var title: String? = null
    var iconUrl: String? = null
    var linkUrl: String? = null
    var locImgRes = 0
}

class ProfileRedInfo : DTO {
    var type: String? = null
    var time: String? = null
}

