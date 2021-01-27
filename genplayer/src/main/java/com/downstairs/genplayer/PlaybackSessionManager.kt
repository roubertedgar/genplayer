package com.downstairs.genplayer

import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaStatus
import com.downstairs.genplayer.notification.NotificationListener
import com.downstairs.genplayer.notification.PlayerNotification
import com.downstairs.genplayer.session.PlayerMediaSession
import javax.inject.Inject

class PlaybackSessionManager @Inject constructor(
    private val mediaSession: PlayerMediaSession,
    private val playerNotification: PlayerNotification
) {

    fun setNotificationListener(listener: NotificationListener) {
        playerNotification.setNotificationListener(listener)
    }

    fun setMediaActionListener(listener: (MediaAction) -> Unit) {
        mediaSession.setMediaActionListener(listener)
    }

    fun post(mediaStatus: MediaStatus) {
        mediaSession.setMediaStatus(mediaStatus)
    }

    fun release() {
        playerNotification.removeNotification()
        mediaSession.release()
    }
}