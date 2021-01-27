package com.downstairs.genplayer.session

import android.graphics.Bitmap

data class MediaSessionData(
    val title: String,
    val description: String,
    val artwork: Bitmap,
    val isPlaying: Boolean
)