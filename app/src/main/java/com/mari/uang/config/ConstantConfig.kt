package com.mari.uang.config

object ConstantConfig {
    const val MODULE_HOME = "1001"
    const val MODULE_ORDER = "1002"
    const val MODULE_DIALOG = "1003"

    const val POSITION_LARGE = "1"

    const val JUMP_SYSTEM_SETTING_REQ_CODE = 100
    const val ORDER_TAB_KEY = "tab"
    const val ID_KEY = "idKey"
    const val PRODUCT_ID_KEY = "product_id"
    const val ORDER_NO_KEY = "orderNoKey"
    const val TITLE_KEY = "titleKey"
    const val TASK_TYPE_KEY = "taskTypeKey"
    const val STATUS_KEY = "statusKey"
    const val WEB_URL_KEY = "url"
    const val ORDER_TYPE_KEY = "orderTypeKey"
    const val MODULE_ID_KEY = "moduleIdKey"
    const val POSITION_ID_KEY = "position_id"
    const val POSITION_KEY = "position"
    const val PERMISSIONS_KEY = "Permissions"

    const val INFO_AUTH_ITEM_TYPE_ENUM = "enum"
    const val INFO_AUTH_ITEM_TYPE_CITY_SELECT = "citySelect"
    const val INFO_AUTH_ITEM_TYPE_TIP = "tip"
    const val INFO_AUTH_ITEM_TYPE_TXT = "txt"

    //上传数据类型
    const val TYPE_SMS = "1"
    const val TYPE_APP = "2"
    const val TYPE_CONTACT = "3"


    const val LOGIN_AGREEMENT_URL = "/#/privacyAgreement"
}

object AFAction {
    //启动
    const val APP_START = "af_app_install"

    //欢迎页
    const val APP_SPLASH = "af_app_splash"

    //权限获取成功
    const val APP_PERMISSIONS_GET = "af_app_Permissions"

    //首页
    const val APP_HOME_PAGE = "af_app_Home_page"

    //产品点击
    const val APP_CLICK_PRODUCT = "af_app_click_product"

    //个人中心按钮
    const val APP_CLICK_TAB_MINE = "af_app_click_mine"

    //登录页
    const val LOGIN_PAGE_INIT = "af_login_page_init"

    //输入手机号
    const val LOGIN_PHONTNUM_INPUT = "af_login_phonenum_input"

    //验证码输入
    const val LOGIN_CAPTCHA_INPUT = "af_login_captcha_input"

    //SMS验证码获取点击
    const val LOGIN_SMS_CODE_CLICK = "af_login_get_captcha"
    const val LOGIN_SMS_CODE_GET_SUCCESS = "af_login_get_captcha_success"
    const val LOGIN_SMS_CODE_GET_FAIL = "af_login_get_captcha_fail"

    //语音验证码获取点击
    const val LOGIN_VOICE_CODE_CLICK = "af_login_get_voice_captcha"
    const val LOGIN_VOICE_CODE_GET_SUCCESS = "af_login_get_voice_captcha_success"
    const val LOGIN_VOICE_CODE_GET_FAIL = "af_login_get_voice_captcha_fail"

    //点击返回
    const val LOGIN_CLICK_BACK = "af_login_click_back"

    //手机格式校验失败
    const val LOGIN_PONE_NUMBER_MATCH_FAIL = "af_login_phone_match_fail"

    //登录按钮点击
    const val LOGIN_CLICK = "af_login_click"
    const val LOGIN_SUCCESS = "af_login_success"
    const val LOGIN_FAIL = "af_login_fail"
}