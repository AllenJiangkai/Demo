package com.de.danaemas.module.home.dto

import com.coupang.common.network.DTO

class NoticeInfo : DTO {
    var product_type = 0
    var product_id = 0
    var message: String? = null
    var url: String? = null
    var isCopyPhone: String? = null
}