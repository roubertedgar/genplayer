package com.downstairs.genplayer.session

import android.content.Context
import android.content.IntentFilter
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
    private val mediaSessionCompat: MediaSessionCompat,
    private val artLoader: ArtworkLoader
) {

    private val actionReceiver = NotificationActionReceiver()
    private var actionListener: (MediaAction) -> Unit = {}
    private var onDataChanged: (MediaSessionData) -> Unit = { }

    val sessionToken: MediaSessionCompat.Token = mediaSessionCompat.sessionToken

    init {
        setMediaSessionCallback()
        registerMediaActionReceiver()
    }

    fun setMediaActionListener(actionListener: (MediaAction) -> Unit) {
        this.actionListener = actionListener
        actionReceiver.setMediaActionListener(actionListener)
    }

    fun setMediaSessionDataListener(onChange: (MediaSessionData) -> Unit) {
        onDataChanged = onChange
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

            val mediaSessionData = MediaSessionData(
                mediaStatus.title, mediaStatus.description, artwork, mediaStatus.isPlaying
            )

            mediaSessionCompat.setMetadata(metadata)
            onDataChanged(mediaSessionData)
        }
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

