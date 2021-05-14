package com.downstairs.genplayer.content

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import java.util.*

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
            .setDrmUuid(C.WIDEVINE_UUID)
            .setDrmLicenseUri("https://license-global.pallycon.com/ri/licenseManager")
            .setDrmLicenseRequestHeaders(mapOf("pallycon-customdata-v2" to "eyJrZXlfcm90YXRpb24iOmZhbHNlLCJyZXNwb25zZV9mb3JtYXQiOiJvcmlnaW5hbCIsInVzZXJfaWQiOiJMSUNFTlNFVE9LRU4iLCJkcm1fdHlwZSI6IldpZGV2aW5lIiwic2l0ZV9pZCI6IkxYUTAiLCJoYXNoIjoiY1JiM3MxRm5hbmh2MXI4Tis2ZTlmMjBHc0VWNTFmSmZrb1lsdStubW9QUT0iLCJjaWQiOiJqeVI1d05CTDc0IiwicG9saWN5IjoiQ0FZdEQ0Y1FyZ0xWXC9hbTh2RktVSnc9PSIsInRpbWVzdGFtcCI6IjIwMjEtMTItMjhUMjA6MTY6MDZaIn0="))
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