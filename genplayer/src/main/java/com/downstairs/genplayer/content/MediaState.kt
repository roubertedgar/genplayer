package com.downstairs.genplayer.content

import android.graphics.Bitmap

data class MediaState(
    val title: String = "",
    val description: String = "",
    val durationMs: Long = -1,
    val positionMs: Long = 0,
    val playbackSpeed: Float = 1f,
    val isPlaying: Boolean = false,
    val artworkUrl: String = "",
    val artwork: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
)