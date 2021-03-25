package com.downstairs.genplayer.session

import android.graphics.Bitmap
import android.media.MediaMetadata
import android.support.v4.media.session.MediaSessionCompat

val MediaSessionCompat.title: String
    get() = controller.metadata.getString(MediaMetadata.METADATA_KEY_TITLE)

val MediaSessionCompat.content: String
    get() = controller.metadata.getString(MediaMetadata.METADATA_KEY_ARTIST)

val MediaSessionCompat.artwork: Bitmap
    get() = controller.metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)

val MediaSessionCompat.state: Int
    get() = controller.playbackState.state
