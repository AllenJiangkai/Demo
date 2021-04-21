package com.mari.uang.module.home.dto

import com.coupang.common.network.DTO

class CardInfo : DTO {
    var id: String? = null
    var productName: String? = null
    var productLogo: String? = null
    var buttonText: String? = null
    var amountRange: String? = null
    var amountRangeDes: String? = null
    var termInfo: String? = null
    var termInfoDes: String? = null
    var loanRate: String? = null
    var loanRateDes: String? = null
    var termInfoImg: String? = null
    var loanRateImg: String? = null
}