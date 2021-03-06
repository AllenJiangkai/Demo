package com.de.danaemas

import com.alibaba.fastjson.JSONObject
import com.coupang.common.network.ApiResponse
import com.coupang.common.network.EmptyVO
import com.coupang.common.network.NetworkApiFactory
import com.de.danaemas.module.auth.dto.AuthCardInfo
import com.de.danaemas.module.auth.dto.AuthSubmitInfo
import com.de.danaemas.module.auth.dto.CanClickInfo
import com.de.danaemas.module.auth.dto.FaceAuthInfo
import com.de.danaemas.module.basic.dto.BasicItemInfoList
import com.de.danaemas.module.contact.dto.ContactInfo
import com.de.danaemas.module.home.dto.HomeInfo
import com.de.danaemas.module.home.dto.ProductDialogInfo
import com.de.danaemas.module.login.LoginVO
import com.de.danaemas.module.login.dto.VerificationCodeInfo
import com.de.danaemas.module.order.OrderList
import com.de.danaemas.module.product.dto.ProductDetailsInfo
import com.de.danaemas.module.product.dto.ProductUrlInfo
import com.de.danaemas.module.product.dto.SendProductUrlInfo
import com.de.danaemas.module.profile.ProfileItemInfo
import com.de.danaemas.module.profile.ProfileRedInfo
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AppApi {

    @POST("credit-user/smsCodeLogin")
    suspend fun login(@Body body: RequestBody): ApiResponse<LoginVO>

    /**
     * 登录验证码获取
     */
    @POST("credit-user/sendSmsCode")
    suspend fun getLoginCode(@Body body: RequestBody): ApiResponse<VerificationCodeInfo>

    /**
     * 登录验证码获取
     */
    @POST("credit-user/sendVoiceCode")
    suspend fun getLoginVoiceCode(@Body body: RequestBody): ApiResponse<VerificationCodeInfo>

    /**
     * 首页数据
     * @param
     * @return
     */
    @GET("v4/index/home-page")
    suspend fun getHomeData(): ApiResponse<HomeInfo>


    /**
     * 退出登录
     * @return
     */
    @GET("credit-user/logout")
    suspend fun logOut(): ApiResponse<EmptyVO>

    /**
     * 预筛接口
     * @param body
     * @return
     */
    @POST("v3/product/apply")
    suspend fun productApply(@Body body: RequestBody): ApiResponse<ProductDialogInfo>

    /**
     * 产品详情
     * @param body
     * @return
     */
    @POST("v3/product/detail")
    suspend fun productDetail(@Body body: RequestBody): ApiResponse<ProductDetailsInfo>

    /**
     * 人脸认证信息
     * @param maps
     * @return
     */
    @GET("credit-card/get-person-info")
    suspend fun getPersonInfo(@QueryMap maps:@JvmSuppressWildcards Map<String, Any>): ApiResponse<FaceAuthInfo>

    /**
     * 验证次数
     */
    @GET("v3/certify/advance-log")
    suspend fun advanceLog(): ApiResponse<CanClickInfo>

    /**
     * 上传照片
     * @param request
     * @return
     */
    @Multipart
    @POST("picture/upload-image")
    suspend fun uploadImage(
        @Part request: MultipartBody.Part?,
        @PartMap map:@JvmSuppressWildcards  Map<String, Any>
    ): ApiResponse<AuthCardInfo>

    /**
     * 身份信息认证完成提交通知
     * @param maps
     * @return
     */
    @GET("v3/certify/check-face-compare")
    suspend fun submitAuth(): ApiResponse<AuthSubmitInfo>

    /**
     * 保存用户确认的身份信息
     * @param body
     * @return
     */
    @POST("v3/certify/save-base-info")
    suspend fun saveBaseInfo(@Body body: RequestBody): ApiResponse<EmptyVO>

    /**
     * 获取紧急联系人信息
     */
    @POST("v3/certify/ext-info")
    suspend fun extInfo(@Body body: RequestBody): ApiResponse<ContactInfo>

    /**
     * 保存联系人信息
     */
    @POST("v3/certify/save-ext-info")
    suspend fun saveContactInfo(@Body body: RequestBody): ApiResponse<EmptyVO>

    /**
     * 个人中心配置Item
     */
    @POST("v3/personal-center/personal-center")
    suspend fun personalCenter(): ApiResponse<ProfileItemInfo>

    /**
     * 获取个人认证信息
     * @param body
     * @return
     */
    @POST("v3/certify/personal-info")
    suspend fun getPersonalAuthInfo(@Body body: RequestBody): ApiResponse<BasicItemInfoList>

    /**
     * 保存个人认证信息
     * @param body
     * @return
     */
    @POST("v3/certify/save-personal-info")
    suspend fun savePersonalAuthInfo(@Body body: RequestBody): ApiResponse<EmptyVO>


    /**
     * 获取工作认证信息
     * @param body
     * @return
     */
    @POST("v3/certify/job-info")
    suspend fun getJobInfo(@Body body: RequestBody): ApiResponse<BasicItemInfoList>

    /**
     * 保存工作认证信息
     * @param body
     * @return
     */
    @POST("v3/certify/save-job-info")
    suspend fun saveJobInfo(@Body body: RequestBody): ApiResponse<EmptyVO>

    /**
     * 获取银行卡认证信息
     * @param maps
     * @return
     */
    @GET("service/entryorder/card-init")
    suspend fun getBankCardInfo(@QueryMap maps:@JvmSuppressWildcards  Map<String, Any>): ApiResponse<BasicItemInfoList>

    /**
     * 保存银行卡认证信息
     * @param body
     * @return
     */
    @POST("v3/certify/save-bank-info")
    suspend fun saveBankCardInfo(@Body body: RequestBody): ApiResponse<EmptyVO>

    /**
     * 试算接口
     * @param body
     * @return
     */
    @POST("v3/product/trial")
    suspend fun productTrial(@Body body: RequestBody): ApiResponse<EmptyVO>

    /**
     * 订单列表
     */
    @POST("/v3/personal-center/order-list")
    suspend fun orderList(@Body body: RequestBody): ApiResponse<OrderList>

    /**
     * 产品详情提交接口
     * @param body
     * @return
     */
    @POST("v3/product/push")
    suspend fun productPush(@Body body: RequestBody): ApiResponse<SendProductUrlInfo>

    /**
     * 产品详情获取协议跳转地址
     */
    @GET("v3/contract/contract-jump")
    suspend fun contractJump(@QueryMap maps:@JvmSuppressWildcards Map<String, Any>): ApiResponse<ProductUrlInfo>

    /**
     * 上传联系人信息
     * @return
     */
    @FormUrlEncoded
    @POST("credit-info/up-load-contents")
    suspend fun upLoadContents(
        @Field("data") data: String?,
        @Field("id") id: Long,
        @Field("type") type: String?
    ): ApiResponse<EmptyVO>

    /**
     * 场景设备信息上报
     * sceneInfoReport
     */
    @POST("v3/upload-info/device-detail")
    suspend fun uploadDevicesDetail(@Body body: RequestBody?): ApiResponse<EmptyVO>

    /**
     * 设备信息上报
     * @param body
     * @return
     */
    @POST("credit-app/device-report")
    suspend fun uploadDevicesReport(@Body body: RequestBody): ApiResponse<EmptyVO>

    /**
     * 上传定位信息
     * @param body
     * @return
     */
    @POST("credit-info/upload-location")
    suspend fun uploadLocation(@Body body: RequestBody): ApiResponse<EmptyVO>

    /**
     * 上报轮渡设备信息
     * sceneInfoReport
     */
    @POST("/credit-info/upload-device-new")
    @FormUrlEncoded
    suspend fun uploadLunDuInfo(@FieldMap maps:@JvmSuppressWildcards Map<String, Any>): ApiResponse<EmptyVO>

    /**
     * 上报广告渠道信息
     * @param body
     * @return
     */
    @POST("v3/loan-confirm/google-market")
    suspend fun upLoadGoogleMarket(@Body body: RequestBody): ApiResponse<JSONObject>

    /**
     * 用户中心第二个订单状态是否显示
     */
    @GET("v3/personal-center/red-notice")
    suspend fun redNotice(): ApiResponse<ArrayList<ProfileRedInfo>>

    /**
     * crashReport
     */
    @POST("/v3/upload-info/upload-fcm-token")
    fun fcmToken(@Body body: RequestBody): Observable<ApiResponse<Any>>

    /**
     * 首页TAB
     * @return
     */
    @POST("youmi-api/v4/app-start/tabs")
    suspend fun getMainTab(): ApiResponse<EmptyVO>

    /**
     * 埋点数据上报
     * sceneInfoReport
     */
    @POST("/v3/buried-point/upload")
    suspend fun uploadActionData(@Body body: RequestBody): ApiResponse<EmptyVO>
    
    
    companion object {
        val api: AppApi = NetworkApiFactory.create(AppApi::class.java)
    }
}