package com.downstairs.genplayer.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationManagerCompat
import com.downstairs.genplayer.R
import com.downstairs.genplayer.PlayerMediaSession
import com.downstairs.genplayer.content.MediaState
import javax.inject.Inject

class PlayerNotification @Inject constructor(
    private val context: Context,
    private val mediaSession: PlayerMediaSession
) {

    companion object {
        const val PLAYER_NOTIFICATION_ID = 455623
        const val PLAYER_CONTROL_REQUEST_CODE = 23334
        const val PLAYER_CHANNEL_ID = "playerNotificationChannelId"
    }

    private val notificationManger = NotificationManagerCompat.from(context)
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationListener: NotificationListener

    fun setNotificationListener(notificationListener: NotificationListener) {
        this.notificationListener = notificationListener
    }

    fun prepare(mediaState: MediaState) {
        notificationBuilder = NotificationCompat.Builder(context, PLAYER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play_notification)
            .setLargeIcon(mediaState.artwork)
            .setContentTitle(mediaState.title)
            .setContentText(mediaState.description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setStyle(createMediaStyle())

        addNotificationActions(mediaState)
        postNotification(notificationBuilder.build())
    }

    private fun createMediaStyle(): androidx.media.app.NotificationCompat.MediaStyle {
        return androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(1)
            .setMediaSession(mediaSession.sessionToken)
    }

    private fun addNotificationActions(mediaState: MediaState) {
        val playPauseAction = if (mediaState.isPlaying) {
            getAction(MediaNotificationAction.PAUSE)
        } else {
            getAction(MediaNotificationAction.PLAY)
        }

        notificationBuilder.addAction(getAction(MediaNotificationAction.BACKWARD))
        notificationBuilder.addAction(playPauseAction)
        notificationBuilder.addAction(getAction(MediaNotificationAction.FORWARD))
        notificationBuilder.addAction(getAction(MediaNotificationAction.STOP))
    }

    private fun getAction(action: MediaNotificationAction): Action {
        return Action.Builder(action.icon, action.title, createPendingIntent(action)).build()
    }

    private fun createPendingIntent(notificationAction: MediaNotificationAction) =
        PendingIntent.getBroadcast(
            context,
            PLAYER_CONTROL_REQUEST_CODE,
            Intent(notificationAction.filter),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun postNotification(notification: Notification) {
        notificationManger.notify(PLAYER_NOTIFICATION_ID, notification)
        notificationListener.onNotificationPosted(PLAYER_NOTIFICATION_ID, notification)
    }

    fun removeNotification() {
        notificationManger.cancel(PLAYER_NOTIFICATION_ID)
        notificationListener.onNotificationRemoved()
    }

}