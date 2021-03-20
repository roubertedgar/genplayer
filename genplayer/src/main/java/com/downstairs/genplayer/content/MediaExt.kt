package com.downstairs.genplayer.content

import com.google.android.exoplayer2.MediaItem

@Suppress("UNCHECKED_CAST")
val MediaItem.properties: Map<String, Any>
    get() {
        val requestProperties = playbackProperties?.tag as? Map<String, Any>
        return requestProperties ?: mapOf()
    }

inline fun <reified T> MediaItem.getProperty(property: String, defaultValue: T): T {
    return properties[property] as? T ?: defaultValue
}

fun MediaItem.toContent(currentPosition: Long) = Content(
    title = getProperty(MediaProperty.TITLE, ""),
    description = getProperty(MediaProperty.DESCRIPTION, ""),
    source = getProperty(MediaProperty.SOURCE, ""),
    artworkUrl = getProperty(MediaProperty.ARTWORK_URL, ""),
    type = getProperty(MediaProperty.TYPE, ""),
    headers = getProperty(MediaProperty.HEADERS, emptyMap()),
    positionMs = currentPosition
)
