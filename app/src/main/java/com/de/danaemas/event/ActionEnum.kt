package com.de.danaemas.event

enum class ActionEnum (val type: String) {

    /**
     * 1。登录注册
     * 2。身份证
     * 3。人脸
     * 4。个人认证信息
     * 5。工作
     * 6。联系人
     * 7。银行卡
     * 8。产品详情提交
     * 9。借款
     */
    Register("1"), Front("2"), Face("3"), Personal("4"), Job("5"), Contact("6"), BankCard("7"), Push("8"), Loan("9");

}