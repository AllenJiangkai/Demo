package com.de.danaemas.module.auth.dto

import com.coupang.common.network.DTO

class FaceAuthInfo :DTO{
    var message: String? = null
    var item: CarInfo? = null
    var info: PersonalInfo? = null
}
class CarInfo :DTO{
    var id_number_z_picture: String? = null
    var face_recognition_picture: String? = null
}
class PersonalInfo :DTO{
    var mobile: String? = null
    var name: String? = null
    var id_number: String? = null
    var birthday: String? = null
}