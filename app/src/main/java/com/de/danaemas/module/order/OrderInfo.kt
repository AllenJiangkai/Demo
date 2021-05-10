package com.de.danaemas.module.order

import com.coupang.common.network.DTO

class OrderInfo :DTO{
    var orderId: String? = null
    var productId: String? = null
    var inside: String? = null
    var productName: String? = null
    var productLogo: String? = null
    var orderStatus: String? = null
    var date: String? = null
    var noticeText: String? = null
    var buttonText: String? = null
    var orderStatusDesc: String? = null
    var orderAmount: String? = null
    var loanDetailUrl: String? = null
    var buttonUrl: String? = null
    var dateText: String? = null
    var moneyText: String? = null
    var loanTime: String? = null
    var repayTime: String? = null
    var term: String? = null
}

class OrderList :DTO{
    var list:ArrayList<OrderInfo>?=null
}