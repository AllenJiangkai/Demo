package com.de.danaemas.module.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_BANNER
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_BIG_CARD
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_NOTICE
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_PRODUCT_CARD
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_REPAY_NOTICE
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_SMALL_CARD
import com.de.danaemas.module.home.dto.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class HomeModel : BaseViewModel<HomeRepository>() {

    val homeList = MutableLiveData<ArrayList<MultiItemEntity>>()
    val homeIcon = MutableLiveData<HomeIcon>()
    val dialogInfo = MutableLiveData<ProductDialogInfo>()
    val networkError = MutableLiveData<Boolean>()

    override fun createRepository(): HomeRepository {
        return HomeRepository()
    }

    fun requestHomeList() {
        viewModelScope.launch {
            val result = repository.requestHomeList()
            if (result is Result.Success) {
                homeIcon.value=result.data.icon
                homeList.value=convertUI(result.data.list)
            }else {
                networkError.value=true
            }
            hideLoading()
        }
    }
    fun requestProduct(moduleId: String, positionId: String, position: String?, productId: String) {
        showLoading()
        viewModelScope.launch {
            val map: MutableMap<String, Any> = HashMap()
            map["module_id"] = moduleId
            map["position_id"] = positionId
            if (position != null) map["position"] = position
            map["product_id"] = productId
            val result = repository.requestProduct(map)
            if (result is Result.Success) {
                result.data.productId=productId
                dialogInfo.value=result.data
            }
            hideLoading()
        }
    }

    private fun convertUI(list: List<HomeList>?): ArrayList<MultiItemEntity> {
        val result = ArrayList<MultiItemEntity>()
        list?.let {
            it.forEach { item ->
                when(item.type){
                    "BANNER" -> {
                        result.add(
                            HomeAdapterItem(
                                TYPE_RECOMMEND_BANNER, JSON.parseObject(
                                    item.item,
                                    object : TypeReference<ArrayList<BannerInfo>>() {})
                            )
                        )
                    }
                    "LARGE_CARD" -> {
                        result.add(
                            HomeAdapterItem(
                                TYPE_RECOMMEND_BIG_CARD, JSON.parseObject(
                                    item.item,
                                    object : TypeReference<CardInfo>() {})
                            )
                        )
                    }
                    "SMALL_CARD" -> {
                        result.add(
                            HomeAdapterItem(
                                TYPE_RECOMMEND_SMALL_CARD, JSON.parseObject(
                                    item.item,
                                    object : TypeReference<CardInfo>() {})
                            )
                        )
                    }
                    "PRODUCT_LIST" -> {
                        JSON.parseArray(item.item, ProductInfo::class.java).forEach { productInfo ->
                            result.add(HomeAdapterItem(TYPE_RECOMMEND_PRODUCT_CARD, productInfo))
                        }
                    }
                    "ANNOUNCEMENT" -> {
                        result.add(
                            HomeAdapterItem(
                                TYPE_RECOMMEND_NOTICE, JSON.parseObject(
                                    item.item,
                                    object : TypeReference<ArrayList<NoticeInfo>>() {})
                            )
                        )
                    }
                    "REPAY" -> {
                        result.add(
                            HomeAdapterItem(
                                TYPE_RECOMMEND_REPAY_NOTICE, JSON.parseObject(
                                    item.item,
                                    object : TypeReference<ArrayList<RepayNoticeInfo>>() {})
                            )
                        )
                    }
                }
            }
        }
        return result
    }





}