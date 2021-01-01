package com.downstairs.genplayer.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.downstairs.dsplayer.R
import javax.inject.Inject

class ArtworkLoader @Inject constructor(private val context: Context) {

    fun load(artworkUrl: String, onReady: (Bitmap) -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(artworkUrl)
            .error(R.drawable.ic_colored_notification_large)
            .diskCacheStrategy(DiskCacheStrategy.DATA)

            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    onReady(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    errorDrawable?.also { onReady(it.toBitmap()) }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}