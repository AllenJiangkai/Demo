package com.de.danaemas.util.upload

import com.coupang.common.network.DTO

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.util.upload
 * @ClassName:      AppInfoBean
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/20 12:38 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/20 12:38 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class AppInfoBean : DTO{
    var appName: String? = null
    var packageName: String? = null
    var versionName: String? = null
    var versionCode = 0
    var inTime: Long = 0
    var upTime: Long = 0
    var appType = 0
    var flags = 0
    var userId: String? = null
}

class ContactInfoBean : DTO{
    var id: Long = 0
    var mobile //联系人的电话
            : String? = null
    var name //联系人的姓名
            : String? = null
    var up_time: String? = null
    var last_time_used: String? = null
    var last_time_contacted: String? = null
    var times_contacted: String? = null
    var source: String? = null
}

class DeviceInfoBean : DTO{
    var pic_count = 0
    var memory: String? = null
    var last_login_time = 0
    var unuse_sdcard: String? = null
    var gps_longitude: String? = null
    var imsi: String? = null
    var storage: String? = null
    var is_root = 0
    var sdcard: String? = null
    var mac: String? = null
    var scene = 0
    var bettary: String? = null
    var gps_address: String? = null
    var wifi = 0
    var blue_tooth_addr: String? = null
    var idfa: String? = null
    var os_version: String? = null
    var ip: String? = null
    var is_simulator = 0
    var unuse_storage: String? = null
    var carrier: String? = null
    var device_info: String? = null
    var wifi_name: String? = null
    var os_type: String? = null
    var gps_latitude: String? = null
    var imei: String? = null
    var resolution: String? = null
    var brand: String? = null
}