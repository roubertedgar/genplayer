package com.downstairs.genplayer.session

import android.graphics.Bitmap
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

data class MediaSessionStatus(
    val token: MediaSessionCompat.Token,
    val title: String,
    val content: String,
    val largeIcon: Bitmap,
    @PlaybackStateCompat.Actions val actions: List<Long>
)