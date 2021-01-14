package com.downstairs.genplayer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationManagerCompat
import com.downstairs.genplayer.MediaData
import com.downstairs.genplayer.PlayerMediaSession
import com.downstairs.genplayer.R
import com.downstairs.genplayer.content.MediaStatus
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

    init {
        mediaSession.setOnMetadataChangeListener { media, playing ->
            postNotification(media, createActions(playing))
        }

        createPlayerChannel()
    }

    fun setNotificationListener(notificationListener: NotificationListener) {
        this.notificationListener = notificationListener
    }

    private fun postNotification(mediaData: MediaData, actions: List<Action>) {
        notificationBuilder = NotificationCompat.Builder(context, PLAYER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play_notification)
            .setContentTitle(mediaData.title)
            .setContentText(mediaData.description)
            .setLargeIcon(mediaData.artwork)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setStyle(createMediaStyle())
            .setActions(actions)

        postNotification(notificationBuilder.build())
    }

    private fun createMediaStyle(): androidx.media.app.NotificationCompat.MediaStyle {
        return androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(1)
            .setMediaSession(mediaSession.sessionToken)
    }

    private fun createActions(playing: Boolean): List<Action> {
        val playPauseAction = if (playing) {
            getAction(MediaNotificationAction.PAUSE)
        } else {
            getAction(MediaNotificationAction.PLAY)
        }

        return listOf(
            getAction(MediaNotificationAction.BACKWARD),
            playPauseAction,
            getAction(MediaNotificationAction.FORWARD),
            getAction(MediaNotificationAction.STOP)
        )
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

    private fun createPlayerChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PLAYER_CHANNEL_ID, "Player", NotificationManager.IMPORTANCE_NONE
            )

            notificationManger.createNotificationChannel(channel)
        }
    }
}


 fun NotificationCompat.Builder.setActions(actions: List<Action>): NotificationCompat.Builder {
    actions.forEach { addAction(it) }
    return this
}