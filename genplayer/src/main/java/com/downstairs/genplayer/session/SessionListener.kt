package com.downstairs.genplayer.session

import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.notification.NotificationListener

interface SessionListener : NotificationListener {
    fun onMediaActionReceived(action: MediaAction) {}
}

