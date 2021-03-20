package com.downstairs.genplayer.content

import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.ext.cast.MediaItemConverter
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaQueueItem
import org.json.JSONObject
import com.google.android.gms.cast.MediaMetadata as CastMetadata

class CustomHeaderMediaItemConverter : MediaItemConverter {
    companion object {
        private const val KEY_TITLE = "title"
    }

    override fun toMediaQueueItem(mediaItem: MediaItem): MediaQueueItem {
        val metadata = createCastMetadata(mediaItem)
        val customData = JSONObject(mediaItem.properties)
        val headers = mediaItem.getProperty<Map<String, String>>(MediaProperty.HEADERS, mapOf())

        val mediaInfo = MediaInfo.Builder(mediaItem.getProperty(MediaProperty.SOURCE, ""))
            .setMetadata(metadata)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType(mediaItem.getProperty(MediaProperty.TYPE, ""))
            .setCustomData(customData)
            .build()


        return MediaQueueItem.Builder(mediaInfo)
            .setCustomData(JSONObject(headers))
            .build()
    }

    private fun createCastMetadata(mediaItem: MediaItem): CastMetadata {
        return CastMetadata(CastMetadata.MEDIA_TYPE_MOVIE).apply {
            putString(KEY_TITLE, mediaItem.getProperty(MediaProperty.TITLE, ""))
        }
    }

    override fun toMediaItem(mediaQueueItem: MediaQueueItem): MediaItem {
        val customMediaData = mediaQueueItem.media.customData ?: return MediaItem.Builder().build()

        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(customMediaData.getString(MediaProperty.TITLE))
            .build()

        return MediaItem.Builder()
            .setMediaId(customMediaData.getString(MediaProperty.ID))
            .setUri(Uri.parse(customMediaData.getString(MediaProperty.SOURCE)))
            .setMediaMetadata(mediaMetadata)
            .setMimeType(customMediaData.getString(MediaProperty.TYPE))
            .setTag(customDataToTagProperties(customMediaData))
            .build()
    }

    private fun customDataToTagProperties(tagJson: JSONObject): Map<String, Any> {
        val tagProperties = mutableMapOf<String, Any>()
        tagJson.keys().forEach { key -> tagProperties[key] = tagJson.get(key) }

        return tagProperties
    }
}