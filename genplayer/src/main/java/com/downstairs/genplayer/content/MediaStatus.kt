package com.downstairs.genplayer.content

data class MediaStatus(
    val title: String = "",
    val description: String = "",
    val durationMs: Long = -1,
    val artworkUrl: String = "",
    val positionMs: Long = 0,
    val playbackSpeed: Float = 1f,
    val isPlaying: Boolean = false
)