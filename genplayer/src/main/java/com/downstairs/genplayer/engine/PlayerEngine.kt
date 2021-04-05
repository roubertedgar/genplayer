package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.content.MediaState
import com.google.android.exoplayer2.Player

abstract class PlayerEngine {

    abstract val player: Player
    abstract val currentContent: Content?
    abstract fun prepare(content: Content)

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

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun forward() {
        val targetPosition = player.currentPosition + 15000
        val position = if (targetPosition > player.duration) player.duration else targetPosition

        player.seekTo(position)
    }

    fun rewind() {
        val targetPosition = player.currentPosition - 15000
        val position = if (targetPosition < 0) 0 else targetPosition

        player.seekTo(position)
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun release() {
        player.stop()
        player.removeListener(listener)
        player.release()
    }

    private fun currentMediaState(): MediaState {
        return currentContent?.let { content ->
            MediaState(
                content.title,
                content.description,
                content.artworkUrl,
                content.positionMs,
                player.duration,
                player.playbackParameters.speed,
                player.isPlaying
            )
        } ?: MediaState()
    }

    fun isContentAlreadyPlaying(content: Content): Boolean {
        return player.isPlaying && content.id != currentContent?.id
    }
}