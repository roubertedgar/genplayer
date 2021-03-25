package com.downstairs.genplayer.content

data class MediaState(
    val title: String = "",
    val description: String = "",
    val artworkUrl: String = "",
    val positionMs: Long = 0,
    val durationMs: Long = -1,
    val playbackSpeed: Float = 1f,
    val isPlaying: Boolean = false
)