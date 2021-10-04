package com.downstairs.genplayer.view.components

enum class PlaybackState(val isPlaying: Boolean) {
    PLAYING(true),
    PAUSED(false),
    BUFFERING(false)
}