package com.de.danaemas.module.home.dto

import com.coupang.common.network.DTO

class HomeInfo : DTO{
    var list: List<HomeList>? = null
    var icon: HomeIcon? = null
}

class HomeIcon:DTO{
    var iconUrl: String? = null
    var linkUrl: String? = null
}
class HomeList:DTO{
    var type: String? = null
    var item: String? = null
}