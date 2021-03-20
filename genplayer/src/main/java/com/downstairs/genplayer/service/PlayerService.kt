package com.downstairs.genplayer.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.downstairs.genplayer.GenPlayer
import com.downstairs.genplayer.session.SessionListener
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaStatus
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.injection.DaggerPlayerComponent
import com.downstairs.genplayer.session.PlayerMediaSession
import javax.inject.Inject

class PlayerService : Service() {

    @Inject
    lateinit var mediaSession: PlayerMediaSession

    @Inject
    lateinit var genPlayer: GenPlayer

    override fun onCreate() {
        DaggerPlayerComponent.factory().create(baseContext).inject(this)

        mediaSession.setSessionListener(object : SessionListener {
            override fun onNotificationPosted(notificationId: Int, notification: Notification) {
                startForeground(notificationId, notification)
            }

            override fun onNotificationRemoved() {
                stopForeground(true)
            }

            override fun onMediaActionReceived(action: MediaAction) {
                onMediaAction(action)
            }
        })

        genPlayer.addEngineListener(object : EngineObserver {

            override fun onStateChanged(mediaStatus: MediaStatus) {
                mediaSession.updateSession(mediaStatus)
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun onMediaAction(action: MediaAction) {
        genPlayer.performAction(action)

        if (action == MediaAction.Stop) {
            mediaSession.release()
        }
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


