package com.downstairs.genplayer.view.components

sealed class PlaybackState(val isClickable: Boolean) {
    object Playing : PlaybackState(true)
    object Paused : PlaybackState(true)
    object Buffering : PlaybackState(false)
}