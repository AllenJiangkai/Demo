package com.mari.uang.module.home.dto

import com.coupang.common.network.DTO

class ProductInfo : DTO {
    var id: String? = null
    var productName: String? = null
    var amountRange: String? = null
    var productDesc: String? = null
    var productLogo: String? = null
    var buttonText: String? = null
    var buttoncolor: String? = null
    var amountRangeDes: String? = null
    var loanRateDes: String? = null
    var buttonStatus: Int? = null
    var productTags: List<String>? = null
}