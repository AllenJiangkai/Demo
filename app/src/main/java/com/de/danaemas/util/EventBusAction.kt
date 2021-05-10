package com.de.danaemas.util

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util
 * @ClassName:      EventBusAction
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/5/2 12:48 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/5/2 12:48 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class EventBusAction<T> constructor(action : String,data :T){
    var data: T? = null
    var action: String? = null
}