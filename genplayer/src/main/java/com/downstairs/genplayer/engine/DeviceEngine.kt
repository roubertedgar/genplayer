package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.Content
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import javax.inject.Inject

class DeviceEngine @Inject constructor(override val player: SimpleExoPlayer) : PlayerEngine() {

    override fun getCurrentItem(): MediaItem? {
        return player.currentMediaItem
    }

    override fun prepare(content: Content) {
        player.playWhenReady = true
        player.setMediaItem(content.buildMediaItem(), content.positionMs)
        player.prepare()
    }

    override fun isContentAlreadyPlaying(content: Content): Boolean {
        return player.isPlaying && player.currentMediaItem?.mediaId == content.contentId
    }
}