package com.de.danaemas.module.contact.dto

import android.text.TextUtils
import com.coupang.common.network.DTO

class ContactInfo : DTO {
    var emergent :UrgentContactInfo?=null
}

class UrgentContactInfo : DTO {
   var lineal_list: List<NameTypeInfo>? = null
   var other_list: List<NameTypeInfo>? = null

   var linealName: String? = null
   var linealRelation: String? = null
   var linealMobile: String? = null
   var otherName: String? = null
   var otherRelation: String? = null
   var otherMobile: String? = null
   var alternatePhone: String? = null


}
class NameTypeInfo : DTO {
   var name: String? = null
   var type: String? = null

    fun nameToType(list: List<NameTypeInfo>?, name: String): String? {
        var type  = ""
        if (list == null || list.isEmpty() || TextUtils.isEmpty(name)) {
            return type
        }
        for (bean in list) {
            if (bean.name == name) {
                type = bean.type ?:""
            }
        }
        return type
    }


    override fun toString(): String {
        return name?:""
    }



}