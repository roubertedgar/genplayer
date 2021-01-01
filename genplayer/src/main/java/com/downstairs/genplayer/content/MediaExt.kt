package com.downstairs.genplayer.content

import com.google.android.exoplayer2.MediaItem

@Suppress("UNCHECKED_CAST")
val MediaItem.properties: Map<MediaProperty, String>
    get() {
        val requestProperties = playbackProperties?.tag as? Map<MediaProperty, String>
        return requestProperties ?: mapOf()
    }

fun MediaItem.getProperty(property: MediaProperty) = properties[property] ?: ""