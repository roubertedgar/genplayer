package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.content.toContent
import com.google.android.exoplayer2.SimpleExoPlayer
import javax.inject.Inject

class DeviceEngine @Inject constructor(override val player: SimpleExoPlayer) : PlayerEngine() {

    override val currentContent: Content?
        get() = player.currentMediaItem?.toContent(player.currentPosition)

    override fun prepare(content: Content) {
        if (!isContentAlreadyPlaying(content)) {
            player.playWhenReady = true
            player.setMediaItem(content.toMediaItem(), content.positionMs)
            player.prepare()
        }
    }
}