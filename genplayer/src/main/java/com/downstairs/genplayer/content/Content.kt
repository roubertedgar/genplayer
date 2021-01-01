package com.downstairs.genplayer.content

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata

data class Content(
    val title: String,
    val description: String,
    val source: String,
    val type: String,
    val cookies: String,
    val positionMs: Long,
    val artworkUrl: String
) {

    val contentId: String
        get() = "$title-$source"

    fun buildMediaItem(): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(title)
            .build()

        return MediaItem.Builder()
            .setMediaId(contentId)
            .setUri(source)
            .setMimeType(type)
            .setMediaMetadata(mediaMetadata)
            .setTag(getCustomData())
            .build()
    }

    private fun getCustomData(): Map<MediaProperty, String> {
        return mapOf(
            MediaProperty.TITLE to title,
            MediaProperty.DESCRIPTION to description,
            MediaProperty.ARTWORK_URL to artworkUrl,
            MediaProperty.COOKIES to cookies,
            MediaProperty.COOKIE to cookies
        )
    }
}