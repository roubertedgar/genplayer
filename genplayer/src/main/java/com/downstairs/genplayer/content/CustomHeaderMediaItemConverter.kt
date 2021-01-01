package com.downstairs.genplayer.content

import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.ext.cast.MediaItemConverter
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaQueueItem
import org.json.JSONObject

class CustomHeaderMediaItemConverter : MediaItemConverter {
    companion object {
        private const val KEY_MEDIA_ID = "mediaId"
        private const val KEY_MEDIA_ITEM = "mediaItem"
        private const val KEY_URI = "uri"
        private const val KEY_TITLE = "title"
        private const val KEY_MIME_TYPE = "mimeType"
    }

    override fun toMediaQueueItem(mediaItem: MediaItem): MediaQueueItem {
        val playbackProperties =
            mediaItem.playbackProperties ?: throw Throwable("Invalid playback properties")

        val metadata =
            com.google.android.gms.cast.MediaMetadata(com.google.android.gms.cast.MediaMetadata.MEDIA_TYPE_MOVIE)
                .apply {
                    putString(KEY_TITLE, mediaItem.mediaMetadata.title)
                }

        val mediaInfo = MediaInfo.Builder(playbackProperties.uri.toString())
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType(playbackProperties.mimeType)
            .setMetadata(metadata)
            .setCustomData(mediaItemToJson(mediaItem))
            .build()

        return MediaQueueItem.Builder(mediaInfo)
            .setCustomData(getCustomRequestData(mediaItem))
            .build()
    }

    private fun mediaItemToJson(mediaItem: MediaItem): JSONObject {
        val playbackProperties =
            mediaItem.playbackProperties ?: throw Throwable("Invalid playback properties")

        val mediaItemJson = JSONObject().apply {
            put(KEY_MEDIA_ID, mediaItem.mediaId)
            put(KEY_TITLE, mediaItem.mediaMetadata.title)
            put(KEY_URI, playbackProperties.uri.toString())
            put(KEY_MIME_TYPE, playbackProperties.mimeType)
        }

        return JSONObject().apply {
            put(KEY_MEDIA_ITEM, mediaItemJson)
        }
    }

    private fun getCustomRequestData(mediaItem: MediaItem) = JSONObject().apply {
        mediaItem.properties.forEach { property ->
            put(property.key.value, property.value)
        }
    }

    override fun toMediaItem(mediaQueueItem: MediaQueueItem): MediaItem {
        val customMediaData = mediaQueueItem.media.customData ?: return MediaItem.Builder().build()

        val mediaItemJson: JSONObject = customMediaData.getJSONObject(KEY_MEDIA_ITEM)

        val builder = MediaItem.Builder().apply {
            setMediaId(mediaItemJson.getString(KEY_MEDIA_ID))
            setUri(Uri.parse(mediaItemJson.getString(KEY_URI)))
            setMediaMetadata(getMediaMetadata(mediaItemJson))
            setMimeType(mediaItemJson.getString(KEY_MIME_TYPE))
            setTag(customDataToTag(mediaQueueItem.customData))
        }

        return builder.build()
    }

    private fun customDataToTag(tagJson: JSONObject): Map<MediaProperty, String> {
        return mutableMapOf<MediaProperty, String>().apply {
            tagJson.keys().forEach { key ->
                put(MediaProperty.from(key), tagJson.getString(key))
            }
        }
    }

    private fun getMediaMetadata(mediaItemJson: JSONObject): MediaMetadata {
        val title = mediaItemJson.getString(KEY_TITLE) ?: ""
        return MediaMetadata.Builder()
            .setTitle(title)
            .build()
    }
}