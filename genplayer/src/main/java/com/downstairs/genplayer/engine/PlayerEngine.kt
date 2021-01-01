package com.downstairs.genplayer.engine

import com.downstairs.dsplayer.content.*
import com.downstairs.genplayer.content.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

abstract class PlayerEngine {

    abstract val player: Player
    abstract fun getCurrentItem(): MediaItem?
    abstract fun prepare(content: Content)
    abstract fun isContentAlreadyPlaying(content: Content): Boolean

    private var observable = EngineObservable()

    private val listener = object : Player.EventListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            observable.onStateChanged(currentMediaState())
        }
    }

    fun setObservable(observable: EngineObservable) {
        player.removeListener(listener)
        player.addListener(listener)

        this.observable = observable
    }

    fun perform(action: MediaAction) {
        when (action) {
            is MediaAction.Play -> player.play()
            is MediaAction.Pause -> player.pause()
            is MediaAction.Forward -> forward()
            is MediaAction.Backward -> backward()
            is MediaAction.SeekTo -> player.seekTo(action.position)
            is MediaAction.Stop -> stopPlayer()
        }
    }

    fun release() {
        stopPlayer()
        player.release()
    }

    private fun stopPlayer() {
        player.removeListener(listener)
        player.stop()
    }

    private fun currentMediaState(): MediaState {
        val currentItem = getCurrentItem()
        val title = currentItem?.getProperty(MediaProperty.TITLE) ?: ""
        val description = currentItem?.getProperty(MediaProperty.DESCRIPTION) ?: ""
        val artworkUrl = currentItem?.getProperty(MediaProperty.ARTWORK_URL) ?: ""

        return MediaState(
            title,
            description,
            player.duration,
            player.currentPosition,
            player.playbackParameters.speed,
            player.isPlaying,
            artworkUrl
        )
    }

    private fun forward() {
        val targetPosition = player.currentPosition + 15000
        val position = if (targetPosition > player.duration) player.duration else targetPosition

        player.seekTo(position)
    }

    private fun backward() {
        val targetPosition = player.currentPosition - 15000
        val position = if (targetPosition < 0) 0 else targetPosition

        player.seekTo(position)
    }
}