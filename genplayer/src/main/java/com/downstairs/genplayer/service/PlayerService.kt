package com.downstairs.genplayer.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.downstairs.genplayer.GenPlayer
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaState
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.injection.DaggerPlayerComponent
import com.downstairs.genplayer.notification.NotificationListener
import com.downstairs.genplayer.notification.PlayerNotification
import com.downstairs.genplayer.session.PlayerMediaSession
import javax.inject.Inject

class PlayerService : Service() {

    @Inject
    lateinit var mediaSession: PlayerMediaSession

    @Inject
    lateinit var notification: PlayerNotification

    @Inject
    lateinit var genPlayer: GenPlayer

    override fun onCreate() {
        DaggerPlayerComponent.factory().create(baseContext).inject(this)

        notification.setMediaSession(mediaSession)

        setupListeners()
    }

    private fun setupListeners() {
        mediaSession.onMediaActionReceived { onMediaAction(it) }

        notification.setNotificationListener(object : NotificationListener {
            override fun onNotificationPosted(notificationId: Int, notification: Notification) {
                startForeground(notificationId, notification)
            }

            override fun onNotificationRemoved() {
                stopForeground(true)
            }
        })

        genPlayer.addEngineListener(object : EngineObserver() {
            override fun onStateChanged(mediaState: MediaState) {
                mediaSession.updateSession(mediaState)
            }
        })
    }

    private fun onMediaAction(action: MediaAction) {
        genPlayer.performAction(action)

        if (action == MediaAction.Stop) {
            mediaSession.release()
            notification.release()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return PLayerServiceBinder()
    }

    inner class PLayerServiceBinder : Binder() {
        fun getPlayer(): GenPlayer {
            return genPlayer
        }
    }
}


