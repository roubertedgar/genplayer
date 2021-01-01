package com.downstairs.genplayer.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.downstairs.genplayer.SplitPlayer
import com.downstairs.genplayer.notification.NotificationListener
import com.downstairs.genplayer.PlaybackSessionManager
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.content.MediaState
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.injection.DaggerPlayerComponent
import javax.inject.Inject

class PlayerService : Service() {

    @Inject
    lateinit var sessionManager: PlaybackSessionManager

    @Inject
    lateinit var splitPlayer: SplitPlayer

    override fun onCreate() {
        DaggerPlayerComponent.factory().create(baseContext).inject(this)

        sessionManager.setMediaActionListener { onMediaAction(it) }

        sessionManager.setNotificationListener(object : NotificationListener {
            override fun onNotificationPosted(notificationId: Int, notification: Notification) {
                startForeground(notificationId, notification)
            }

            override fun onNotificationRemoved() {
                stopForeground(true)
            }
        })

        splitPlayer.addEngineListener(object : EngineObserver {

            override fun onStateChanged(mediaState: MediaState) {
                sessionManager.post(mediaState)
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun onMediaAction(action: MediaAction) {
        splitPlayer.performAction(action)

        if (action == MediaAction.Stop) {
            sessionManager.release()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return PLayerServiceBinder()
    }

    inner class PLayerServiceBinder : Binder() {
        fun getPlayer(): SplitPlayer {
            return splitPlayer
        }
    }
}


