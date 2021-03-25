package com.downstairs.genplayer.session

import android.graphics.Bitmap
import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaState
import com.downstairs.genplayer.tools.ArtworkLoader
import javax.inject.Inject

class PlayerMediaSession @Inject constructor(
    private val sessionCompat: MediaSessionCompat,
    private val sessionReceiver: MediaSessionReceiver,
    private val artLoader: ArtworkLoader
) {

    private lateinit var onSessionChanged: (MediaSessionCompat) -> Unit

    init {
        sessionCompat.setCallback(sessionReceiver)
    }

    fun onMediaActionReceived(onActionReceived: (MediaAction) -> Unit) {
        sessionReceiver.onActionReceived { onActionReceived(it) }
    }

    fun onSessionChanged(onSessionChanged: (MediaSessionCompat) -> Unit) {
        this.onSessionChanged = onSessionChanged
    }

    fun updateSession(mediaState: MediaState) {
        artLoader.load(mediaState.artworkUrl) { artwork -> onArtworkLoaded(mediaState, artwork) }
    }

    fun release() {
        sessionCompat.release()
    }

    private fun onArtworkLoaded(mediaState: MediaState, artwork: Bitmap) {
        updateMetadata(mediaState, artwork)
        updatePlaybackState(mediaState)
        onSessionChanged(sessionCompat)
    }

    private fun updateMetadata(mediaState: MediaState, artwork: Bitmap) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadata.METADATA_KEY_TITLE, mediaState.title)
            .putString(MediaMetadata.METADATA_KEY_ARTIST, mediaState.description)
            .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, artwork)
            .putLong(MediaMetadata.METADATA_KEY_DURATION, mediaState.durationMs)
            .build()

        sessionCompat.setMetadata(metadata)
    }

    private fun updatePlaybackState(mediaState: MediaState) {
        val state = getState(mediaState)

        val playbackState = PlaybackStateCompat.Builder()
            .setState(state, mediaState.positionMs, mediaState.playbackSpeed)
            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
            .build()

        sessionCompat.setPlaybackState(playbackState)
    }

    private fun getState(mediaState: MediaState) =
        if (mediaState.isPlaying) PlaybackStateCompat.STATE_PLAYING
        else PlaybackStateCompat.STATE_PAUSED
}
