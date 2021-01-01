package com.downstairs.genplayer

import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaState
import com.downstairs.genplayer.notification.NotificationListener
import com.downstairs.genplayer.notification.PlayerNotification
import com.downstairs.genplayer.tools.ArtworkLoader
import javax.inject.Inject

class PlaybackSessionManager @Inject constructor(
    private val mediaSession: PlayerMediaSession,
    private val artworkLoader: ArtworkLoader,
    private val playerNotification: PlayerNotification
) {

    fun setNotificationListener(listener: NotificationListener) {
        playerNotification.setNotificationListener(listener)
    }

    fun setMediaActionListener(listener: (MediaAction) -> Unit) {
        mediaSession.setMediaActionListener(listener)
    }

    fun post(mediaState: MediaState) {
        loadArtwork(mediaState)
    }

    private fun loadArtwork(mediaState: MediaState) {
        artworkLoader.load(mediaState.artworkUrl) { artwork ->
            onArtLoaded(mediaState.copy(artwork = artwork))
        }
    }

    private fun onArtLoaded(mediaState: MediaState) {
        mediaSession.setMediaState(mediaState)
        playerNotification.prepare(mediaState)
    }

    fun release() {
        playerNotification.removeNotification()
        mediaSession.release()
    }
}