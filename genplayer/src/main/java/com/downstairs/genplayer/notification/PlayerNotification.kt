package com.downstairs.genplayer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationManagerCompat
import com.downstairs.genplayer.R
import com.downstairs.genplayer.session.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.media.app.NotificationCompat.MediaStyle as MediaStyle

class PlayerNotification @Inject constructor(private val context: Context) {

    companion object {
        const val PLAYER_NOTIFICATION_ID = 455623
        const val PLAYER_CONTROL_REQUEST_CODE = 23334
        const val PLAYER_CHANNEL_NAME = "Player"
        const val PLAYER_CHANNEL_ID = "playerNotificationChannelId"
    }

    private val notificationManger = NotificationManagerCompat.from(context)
    private lateinit var notificationListener: NotificationListener

    init {
        createPlayerChannel()
    }

    fun setNotificationListener(listener: NotificationListener) {
        this.notificationListener = listener
    }

    fun setMediaSession(mediaSession: PlayerMediaSession) {
        mediaSession.onSessionChanged {
            postNotification(it)
        }
    }

    private fun postNotification(mediaSession: MediaSessionCompat) {
        val notification = NotificationCompat.Builder(context, PLAYER_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.drawable.ic_play_notification)
            .setContentTitle(mediaSession.title)
            .setContentText(mediaSession.content)
            .setLargeIcon(mediaSession.artwork)
            .setStyle(createMediaStyle(mediaSession.sessionToken))
            .setActions(createNotificationActions(mediaSession.state))
            .build()

        postNotification(notification)
    }

    private fun createNotificationActions(state: Int): List<Action> {
        val playPauseAction = if (state == PlaybackStateCompat.STATE_PLAYING) {
            buildAction(MediaNotificationAction.PAUSE)
        } else {
            buildAction(MediaNotificationAction.PLAY)
        }

        return listOf(
            buildAction(MediaNotificationAction.REWIND),
            playPauseAction,
            buildAction(MediaNotificationAction.FORWARD),
            buildAction(MediaNotificationAction.STOP)
        )
    }

    private fun buildAction(action: MediaNotificationAction): Action {
        return Action.Builder(action.icon, action.title, createPendingIntent(action)).build()
    }

    private fun createMediaStyle(sessionToken: MediaSessionCompat.Token): MediaStyle {
        return MediaStyle()
            .setShowActionsInCompactView(1)
            .setMediaSession(sessionToken)
    }

    private fun postNotification(notification: Notification) {
        notificationManger.notify(PLAYER_NOTIFICATION_ID, notification)
        notificationListener.onNotificationPosted(PLAYER_NOTIFICATION_ID, notification)
    }

    private fun createPendingIntent(notificationAction: MediaNotificationAction): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            PLAYER_CONTROL_REQUEST_CODE,
            Intent(notificationAction.filter),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createPlayerChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PLAYER_CHANNEL_ID,
                PLAYER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE
            )

            notificationManger.createNotificationChannel(channel)
        }
    }

    fun removeNotification() {
        GlobalScope.launch {
            delay(150)
            notificationManger.cancel(PLAYER_NOTIFICATION_ID)
            notificationListener.onNotificationRemoved()
        }
    }
}

private fun NotificationCompat.Builder.setActions(actions: List<Action>): NotificationCompat.Builder {
    actions.forEach { addAction(it) }
    return this
}
