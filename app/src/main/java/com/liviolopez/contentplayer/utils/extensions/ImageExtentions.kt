package com.liviolopez.contentplayer.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.signature.ObjectKey
import com.liviolopez.contentplayer.R

@GlideModule
class BaseGlideApp : AppGlideModule() {
    var factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val decodeFormat = DecodeFormat.PREFER_ARGB_8888
        builder.apply {
            val calculator = MemorySizeCalculator.Builder(context).build()
            setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
            setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))

            RequestOptions()
                .format(decodeFormat)
                .signature(ObjectKey(System.currentTimeMillis().toShort()))
        }
    }
}

fun ImageView.setImage(source: Any, errorSource: Drawable? = null){
    val imageView = this

    val initDrawable = ContextCompat.getDrawable(context, R.drawable.ic_broken_link)

    val glide = GlideApp.with(context)
        .load(source)
        .transition(withCrossFade(BaseGlideApp().factory))
        .error(errorSource ?: initDrawable)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                imageView.setBackgroundResource(0)
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                imageView.setBackgroundResource(0)
                return false
            }
        })

    glide.into(imageView)
}