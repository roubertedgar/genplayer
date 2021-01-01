package com.downstairs.genplayer

import android.content.Context
import android.content.IntentFilter
import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaState
import com.downstairs.genplayer.notification.MediaNotificationAction
import com.downstairs.genplayer.notification.NotificationActionReceiver
import javax.inject.Inject

class PlayerMediaSession @Inject constructor(private val context: Context) {

    companion object {
        const val MEDIA_SESSION_TAG = "MediaSessionPlayerTag"
    }

    private val actionReceiver = NotificationActionReceiver()
    private val mediaSessionCompat = MediaSessionCompat(context, MEDIA_SESSION_TAG)
    private var actionListener: (MediaAction) -> Unit = {}

    val sessionToken: MediaSessionCompat.Token = mediaSessionCompat.sessionToken

    fun setMediaActionListener(actionListener: (MediaAction) -> Unit) {
        this.actionListener = actionListener
        actionReceiver.setMediaActionListener(actionListener)
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

    fun setMediaState(mediaState: MediaState) {
        setMetadata(mediaState)
        setPlaybackState(mediaState)
    }

    private fun setMetadata(mediaState: MediaState) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadata.METADATA_KEY_TITLE, mediaState.title)
            .putString(MediaMetadata.METADATA_KEY_ARTIST, mediaState.description)
            .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, mediaState.artwork)
            .putLong(MediaMetadata.METADATA_KEY_DURATION, mediaState.durationMs)
            .build()

        mediaSessionCompat.setMetadata(metadata)
    }

    private fun setPlaybackState(mediaState: MediaState) {
        val state = if (mediaState.isPlaying) {
            PlaybackStateCompat.STATE_PLAYING
        } else {
            PlaybackStateCompat.STATE_PAUSED
        }

        val playbackState = PlaybackStateCompat.Builder()
            .setState(state, mediaState.positionMs, mediaState.playbackSpeed)
            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
            .build()

        mediaSessionCompat.setPlaybackState(playbackState)
    }

    fun release() {
        mediaSessionCompat.release()
    }
}