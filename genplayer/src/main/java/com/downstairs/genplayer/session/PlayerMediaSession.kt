package com.downstairs.genplayer.session

import android.content.Context
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.downstairs.genplayer.content.MediaStatus
import com.downstairs.genplayer.notification.*
import com.downstairs.genplayer.tools.ArtworkLoader
import javax.inject.Inject

class PlayerMediaSession @Inject constructor(
    private val context: Context,
    private val mediaSession: MediaSessionCompat,
    private val notification: PlayerNotification,
    private val artLoader: ArtworkLoader
) {

    private val actionReceiver = MediaActionReceiver()

    init {
        registerMediaActionReceiver()
    }

    fun setSessionListener(sessionListener: SessionListener) {
        mediaSession.setCallback(MediaSessionCallback(sessionListener))
        actionReceiver.setMediaActionListener(sessionListener)
        notification.setNotificationListener(sessionListener)
    }

    fun updateSession(mediaStatus: MediaStatus) {
        artLoader.load(mediaStatus.artworkUrl) { artwork ->
            onArtworkLoaded(mediaStatus, artwork)
        }
    }

    fun release() {
        mediaSession.release()
        notification.removeNotification()
    }

    private fun registerMediaActionReceiver() {
        MediaNotificationAction.forEach { action ->
            context.registerReceiver(actionReceiver, IntentFilter(action.filter))
        }
    }

    private fun onArtworkLoaded(mediaStatus: MediaStatus, artwork: Bitmap) {
        val actions = getSupportedActions(mediaStatus)

        fun notifySessionChanged() {
            notification.postNotification(
                MediaSessionStatus(
                    mediaSession.sessionToken,
                    mediaStatus.title,
                    mediaStatus.description,
                    artwork,
                    actions
                )
            )
        }

        setMetadata(mediaStatus, artwork)
        setPlaybackState(mediaStatus, actions)
        notifySessionChanged()
    }

    private fun setMetadata(mediaStatus: MediaStatus, artwork: Bitmap) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadata.METADATA_KEY_TITLE, mediaStatus.title)
            .putString(MediaMetadata.METADATA_KEY_ARTIST, mediaStatus.description)
            .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, artwork)
            .putLong(MediaMetadata.METADATA_KEY_DURATION, mediaStatus.durationMs)
            .build()

        mediaSession.setMetadata(metadata)
    }

    private fun setPlaybackState(
        mediaStatus: MediaStatus,
        @PlaybackStateCompat.Actions actions: List<Long>
    ) {
        val state = getState(mediaStatus)

        val playbackState = PlaybackStateCompat.Builder()
            .setState(state, mediaStatus.positionMs, mediaStatus.playbackSpeed)
            .setActions(actions)
            .build()

        mediaSession.setPlaybackState(playbackState)
    }

    private fun getState(mediaStatus: MediaStatus) =
        if (mediaStatus.isPlaying) PlaybackStateCompat.STATE_PLAYING
        else PlaybackStateCompat.STATE_PAUSED

    private fun getSupportedActions(status: MediaStatus): List<Long> {
        val playPauseAction = if (status.isPlaying) PlaybackStateCompat.ACTION_PAUSE
        else PlaybackStateCompat.ACTION_PLAY

        return listOf(
            PlaybackStateCompat.ACTION_REWIND,
            playPauseAction,
            PlaybackStateCompat.ACTION_FAST_FORWARD,
            PlaybackStateCompat.ACTION_STOP,
            PlaybackStateCompat.ACTION_SEEK_TO
        )
    }

    private fun PlaybackStateCompat.Builder.setActions(
        @PlaybackStateCompat.Actions actions: List<Long>
    ): PlaybackStateCompat.Builder {
        setActions(actions.reduce { acc, action -> acc or action })
        return this
    }
}
