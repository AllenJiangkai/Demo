package com.de.danaemas.module.home

import android.content.Intent
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.coupang.common.user.UserManager
import com.de.danaemas.R
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_BANNER
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_BIG_CARD
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_NOTICE
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_PRODUCT_CARD
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_REPAY_NOTICE
import com.de.danaemas.module.home.HomeAdapterItem.Companion.TYPE_RECOMMEND_SMALL_CARD
import com.de.danaemas.module.home.dto.*
import com.de.danaemas.module.home.widget.*
import com.de.danaemas.module.login.LoginActivity
import com.de.danaemas.util.RouterUtil


class HomeAdapter(data: MutableList<MultiItemEntity>? = null) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {


    var iconUrl :String?=null
    var linkUrl :String?=null

    init {
        addItemType(TYPE_RECOMMEND_BANNER, R.layout.item_banner_view)
        addItemType(TYPE_RECOMMEND_BIG_CARD, R.layout.item_big_card_view)
        addItemType(TYPE_RECOMMEND_SMALL_CARD, R.layout.item_small_card_view)
        addItemType(TYPE_RECOMMEND_PRODUCT_CARD, R.layout.item_product_card_view)
        addItemType(TYPE_RECOMMEND_NOTICE, R.layout.item_notice_view)
        addItemType(TYPE_RECOMMEND_REPAY_NOTICE, R.layout.item_repay_notice_view)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity?) {
        val homeEntity = item as HomeAdapterItem<*>
        when (homeEntity.itemType) {
            TYPE_RECOMMEND_BANNER -> {
                convertBanner(helper, homeEntity)
            }
            TYPE_RECOMMEND_BIG_CARD -> {
                convertBigCard(helper, homeEntity)
            }
            TYPE_RECOMMEND_SMALL_CARD -> {
                convertSmallCard(helper, homeEntity)
            }
            TYPE_RECOMMEND_PRODUCT_CARD -> {
                convertProductCard(helper, homeEntity)
            }
            TYPE_RECOMMEND_NOTICE -> {
                convertNotice(helper, homeEntity)
            }
            TYPE_RECOMMEND_REPAY_NOTICE -> {
                convertRepayNotice(helper, homeEntity)
            }
        }
    }

    private fun convertBanner(helper: BaseViewHolder, item: HomeAdapterItem<*>) {
        helper.run {
            val bean = item.data as ArrayList<BannerInfo>
            helper.getView<HomeBannerView>(R.id.banner_view).apply {
                initBannerViewData(bean)

                 onItemClickListener { position, bean ->
                     if(!UserManager.isLogin()){
                         mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                     }else {
                         RouterUtil.router(mContext, bean.url)
                     }
                 }

                initRight(iconUrl?:"",linkUrl?:"")
            }
        }
    }
    private fun convertBigCard(helper: BaseViewHolder, item: HomeAdapterItem<*>) {
        helper.run {
            val bean = item.data as CardInfo
            helper.getView<BigCardView>(R.id.big_card_view).apply {
                initData(bean)
            }
        }
    }
    private fun convertSmallCard(helper: BaseViewHolder, item: HomeAdapterItem<*>) {
        helper.run {
            val bean = item.data as CardInfo
            helper.getView<SmallCardView>(R.id.small_card_view).apply {
                initData(bean)
            }
        }
    }
    private fun convertProductCard(helper: BaseViewHolder, item: HomeAdapterItem<*>) {
        helper.run {
            val bean = item.data as ProductInfo
            helper.getView<ProductCardView>(R.id.product_view).apply {
                initData(bean)
            }
        }
    }

    private fun convertNotice(helper: BaseViewHolder, item: HomeAdapterItem<*>) {
        helper.run {
            val bean = item.data as ArrayList<NoticeInfo>
            helper.getView<NoticeView>(R.id.notice_view).apply {
                initData(bean)
            }
        }
    }

    private fun convertRepayNotice(helper: BaseViewHolder, item: HomeAdapterItem<*>) {
        helper.run {
            val bean = item.data as ArrayList<RepayNoticeInfo>
            helper.getView<RepayNoticeView>(R.id.repay_notice_view).apply {
                initData(bean)
            }
        }
    }


}