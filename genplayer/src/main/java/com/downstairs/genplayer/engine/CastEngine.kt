package com.downstairs.genplayer.engine

import android.content.Context
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.content.CustomHeaderMediaItemConverter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.gms.cast.framework.CastContext
import javax.inject.Inject

class CastEngine @Inject constructor(context: Context) : PlayerEngine() {

    private val castContext = CastContext.getSharedInstance(context)
    private val mediaItemConverter = CustomHeaderMediaItemConverter()
    override val player = CastPlayer(castContext, mediaItemConverter)

    override fun getCurrentItem(): MediaItem? {
        return castContext.currentItem?.let {
            mediaItemConverter.toMediaItem(it)
        }
    }

    override fun prepare(content: Content) {
        player.setMediaItem(content.buildMediaItem(), content.positionMs)
    }

    override fun isContentAlreadyPlaying(content: Content): Boolean {
        return player.isPlaying && content.contentId == getCurrentItem()?.mediaId
    }

    fun setSessionChangeListener(listener: SessionAvailabilityListener) {
        player.setSessionAvailabilityListener(listener)
    }

    fun isCastSessionAvailable(): Boolean = player.isCastSessionAvailable
}