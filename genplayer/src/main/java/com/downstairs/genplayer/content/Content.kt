package com.downstairs.genplayer.content

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata

data class Content(
    val title: String,
    val description: String,
    val source: String,
    val artworkUrl: String,
    val type: String,
    val headers: Map<String, String>,
    val positionMs: Long
) {

    val id: String
        get() = "$title-$source"

    fun toMediaItem(): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(title)
            .build()

        return MediaItem.Builder()
            .setMediaId(id)
            .setUri(source)
            .setMimeType(type)
            .setMediaMetadata(mediaMetadata)
            .setTag(getCustomData())
            .build()
    }

    private fun getCustomData(): Map<String, Any> {
        return mapOf(
            MediaProperty.ID to id,
            MediaProperty.TITLE to title,
            MediaProperty.DESCRIPTION to description,
            MediaProperty.SOURCE to source,
            MediaProperty.TYPE to type,
            MediaProperty.ARTWORK_URL to artworkUrl,
            MediaProperty.HEADERS to headers
        )
    }
}