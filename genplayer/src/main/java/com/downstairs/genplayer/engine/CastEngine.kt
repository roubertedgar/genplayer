package com.downstairs.genplayer.engine

import android.content.Context
import com.downstairs.genplayer.content.*
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.gms.cast.framework.CastContext
import javax.inject.Inject

class CastEngine @Inject constructor(context: Context) : PlayerEngine() {

    private val castContext = CastContext.getSharedInstance(context)
    private val mediaItemConverter = CustomHeaderMediaItemConverter()
    override val player = CastPlayer(castContext, mediaItemConverter)

    override val currentContent: Content?
        get() = castContext.currentItem?.let {
            mediaItemConverter.toMediaItem(it).toContent(player.currentPosition)
        }

    override fun prepare(content: Content) {
        if (!isContentAlreadyPlaying(content)) {
            player.setMediaItem(content.toMediaItem(), content.positionMs)
        }
    }

    fun setSessionChangeListener(listener: SessionAvailabilityListener) {
        player.setSessionAvailabilityListener(listener)
    }

    fun isCastSessionAvailable(): Boolean = player.isCastSessionAvailable
}