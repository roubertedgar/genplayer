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
import com.downstairs.genplayer.session.MediaSessionStatus
import com.downstairs.genplayer.session.SessionListener
import javax.inject.Inject

class PlayerNotification @Inject constructor(private val context: Context) {

    companion object {
        const val PLAYER_NOTIFICATION_ID = 455623
        const val PLAYER_CONTROL_REQUEST_CODE = 23334
        const val PLAYER_CHANNEL_NAME = "Player"
        const val PLAYER_CHANNEL_ID = "playerNotificationChannelId"
    }

    private val notificationManger = NotificationManagerCompat.from(context)
    private lateinit var notificationListener: NotificationListener
    private lateinit var notificationBuilder: NotificationCompat.Builder

    init {
        createPlayerChannel()
    }

    fun setNotificationListener(listener: NotificationListener) {
        this.notificationListener = listener
    }

    fun postNotification(sessionStatus: MediaSessionStatus) {
        notificationBuilder = NotificationCompat.Builder(context, PLAYER_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.drawable.ic_play_notification)
            .setContentTitle(sessionStatus.title)
            .setContentText(sessionStatus.content)
            .setLargeIcon(sessionStatus.largeIcon)
            .setStyle(createMediaStyle(sessionStatus.token))
            .setActions(sessionStatus.actions)

        postNotification(notificationBuilder.build())
    }

    private fun createMediaStyle(sessionToken: MediaSessionCompat.Token): androidx.media.app.NotificationCompat.MediaStyle {
        return androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(1)
            .setMediaSession(sessionToken)
    }

    private fun postNotification(notification: Notification) {
        notificationManger.notify(PLAYER_NOTIFICATION_ID, notification)
        notificationListener.onNotificationPosted(PLAYER_NOTIFICATION_ID, notification)
    }

    fun removeNotification() {
        notificationManger.cancel(PLAYER_NOTIFICATION_ID)
        notificationListener.onNotificationRemoved()
    }

    private fun createPlayerChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PLAYER_CHANNEL_ID, PLAYER_CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE
            )

            notificationManger.createNotificationChannel(channel)
        }
    }

    private fun NotificationCompat.Builder.setActions(sessionActions: List<Long>): NotificationCompat.Builder {
        sessionActions.forEach {
            toNotificationAction(it)?.also { action ->
                addAction(action)
            }
        }

        return this
    }

    private fun toNotificationAction(@PlaybackStateCompat.Actions action: Long) = when (action) {
        PlaybackStateCompat.ACTION_PLAY -> getAction(MediaNotificationAction.PLAY)
        PlaybackStateCompat.ACTION_PAUSE -> getAction(MediaNotificationAction.PAUSE)
        PlaybackStateCompat.ACTION_REWIND -> getAction(MediaNotificationAction.REWIND)
        PlaybackStateCompat.ACTION_FAST_FORWARD -> getAction(MediaNotificationAction.FORWARD)
        PlaybackStateCompat.ACTION_STOP -> getAction(MediaNotificationAction.STOP)
        else -> null
    }


    private fun getAction(action: MediaNotificationAction) =
        Action.Builder(action.icon, action.title, createPendingIntent(action)).build()

    private fun createPendingIntent(notificationAction: MediaNotificationAction) =
        PendingIntent.getBroadcast(
            context,
            PLAYER_CONTROL_REQUEST_CODE,
            Intent(notificationAction.filter),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
}
