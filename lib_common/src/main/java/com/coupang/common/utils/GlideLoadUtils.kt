package com.coupang.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget

object GlideLoadUtils {

    private val options: RequestOptions = RequestOptions()
        .placeholder(0)
        .error(0)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .skipMemoryCache(true)


    fun loadImage(context: Context, url: String?,  simpleTarget: SimpleTarget<Bitmap?>) {
        Glide.with(context).asBitmap().load(url).into(simpleTarget)
    }
    fun loadImage(context: Context, res: Int?, simpleTarget: SimpleTarget<Bitmap?>) {
        Glide.with(context).asBitmap().load(res).into(simpleTarget)
    }

    fun loadImage(context: Context?, url: String?, imageView: ImageView) {
        context?.also {
            Glide.with(context).load(url).apply(options)
                .into(imageView)
        }
    }
}