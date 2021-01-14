package com.downstairs.genplayer

import android.content.Context
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaStatus
import com.downstairs.genplayer.notification.MediaNotificationAction
import com.downstairs.genplayer.notification.NotificationActionReceiver
import com.downstairs.genplayer.tools.ArtworkLoader
import javax.inject.Inject

class PlayerMediaSession @Inject constructor(
    private val context: Context,
    private val artLoader: ArtworkLoader
) {

    companion object {
        const val MEDIA_SESSION_TAG = "MediaSessionPlayerTag"
    }

    private val actionReceiver = NotificationActionReceiver()
    private val mediaSessionCompat = MediaSessionCompat(context, MEDIA_SESSION_TAG)
    private var actionListener: (MediaAction) -> Unit = {}
    private var onMetadataChanged: (data: MediaData, playing: Boolean) -> Unit = { _, _ -> }

    val sessionToken: MediaSessionCompat.Token = mediaSessionCompat.sessionToken

    fun setMediaActionListener(actionListener: (MediaAction) -> Unit) {
        this.actionListener = actionListener
        actionReceiver.setMediaActionListener(actionListener)
    }

    fun setOnMetadataChangeListener(onChange: (data: MediaData, playing: Boolean) -> Unit) {
        onMetadataChanged = onChange
    }

    init {
        setMediaSessionCallback()
        registerMediaActionReceiver()
    }

    private fun setMediaSessionCallback() {
        mediaSessionCompat.setCallback(object : MediaSessionCompat.Callback() {
            override fun onSeekTo(pos: Long) {
                actionListener(MediaAction.SeekTo(pos))
            }
        })
    }

    private fun registerMediaActionReceiver() {
        MediaNotificationAction.forEach { action ->
            context.registerReceiver(actionReceiver, IntentFilter(action.filter))
        }
    }

    fun setMediaStatus(mediaStatus: MediaStatus) {
        setMetadata(mediaStatus)
        setPlaybackState(mediaStatus)
    }

    private fun setMetadata(mediaStatus: MediaStatus) {
        artLoader.load(mediaStatus.artworkUrl) { artwork ->
            val metadata = MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE, mediaStatus.title)
                .putString(MediaMetadata.METADATA_KEY_ARTIST, mediaStatus.description)
                .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, artwork)
                .putLong(MediaMetadata.METADATA_KEY_DURATION, mediaStatus.durationMs)
                .build()

            notifyMediaChanged(mediaStatus, artwork)
            mediaSessionCompat.setMetadata(metadata)
        }
    }

    private fun notifyMediaChanged(mediaStatus: MediaStatus, artwork: Bitmap) {
        onMetadataChanged(
            MediaData(mediaStatus.title, mediaStatus.description, artwork),
            mediaStatus.isPlaying
        )
    }

    private fun setPlaybackState(mediaStatus: MediaStatus) {
        val state = if (mediaStatus.isPlaying) {
            PlaybackStateCompat.STATE_PLAYING
        } else {
            PlaybackStateCompat.STATE_PAUSED
        }

        val playbackState = PlaybackStateCompat.Builder()
            .setState(state, mediaStatus.positionMs, mediaStatus.playbackSpeed)
            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
            .build()

        mediaSessionCompat.setPlaybackState(playbackState)
    }

    fun release() {
        mediaSessionCompat.release()
    }
}

data class MediaData(
    val title: String,
    val description: String,
    val artwork: Bitmap
)