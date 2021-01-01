package com.downstairs.genplayer.notification

import android.app.Notification

interface NotificationListener {
    fun onNotificationPosted(notificationId: Int, notification: Notification) {}
    fun onNotificationRemoved() {}
}