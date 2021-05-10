package com.de.danaemas.module.product.dto

import com.coupang.common.network.DTO

class ProductDetailsInfo : DTO {
    var result = 0
    var productDetail: ProductInfo? = null
    var userInfo: ProductUserInfo? = null
    var nextStep: NextStepInfo? = null
    var agreement: ArrayList<AgreementInfo>? = null
    var verify: ArrayList<VerifyInfo>? = null

}

class ProductInfo : DTO {
    var amountDesc: String? = null
    var amount: String? = null
    var termDesc: String? = null
    var term: String? = null
    var term_type: String? = null
    var id: String? = null
    var productName: String? = null
    var orderNo: String? = null
    var orderId: String? = null
    var columnText: ColumnTextInfo? = null
    var buttonText: String? = null
    var buttonUrl: String? = null
    var url: String? = null
    var hotline: HotlineInfo? = null
    var complaintUrl: String? = null
    var amountArr: ArrayList<String>? = null
    var termArr: ArrayList<String>? = null
}

class ProductUserInfo : DTO {
    var phone: String? = null
    var idNumber: String? = null
    var name: String? = null
}

class NextStepInfo : DTO {
    var step: String? = null
    var url: String? = null
    var title: String? = null
    var type = 0
}

class ColumnTextInfo : DTO {
    var tag1: TextTagInfo? = null
    var tag2: TextTagInfo? = null
}

class TextTagInfo : DTO {
    var title: String? = null
    var text: String? = null

}

class HotlineInfo : DTO {
    var value: String? = null
}

class AgreementInfo : DTO {
    var productId: String? = null
    var scene: String? = null
    var position: String? = null
    var orderId: String? = null
    var title: String? = null
}

class VerifyInfo : DTO {
    var title: String? = null
    var subtitle: String? = null
    var type: String? = null
    var url: String? = null
    var status: String? = null
    var statusName: String? = null
    var taskType: String? = null
    var canClick: String? = null
    var optional: String? = null
    var ifMust: String? = null
    var canClickMessage: String? = null
    var log: String? = null
}



