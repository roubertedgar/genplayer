package com.downstairs.genplayer.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.downstairs.genplayer.content.MediaAction

class NotificationActionReceiver : BroadcastReceiver() {

    private var actionListener: (MediaAction) -> Unit = {}

    fun setMediaActionListener(actionListener: (MediaAction) -> Unit) {
        this.actionListener = actionListener
    }

    override fun onReceive(contxt: Context, intent: Intent) {

        when (intent.action) {
            PLAYER_CONTROL_ACTION_PLAY -> actionListener(MediaAction.Play)
            PLAYER_CONTROL_ACTION_PAUSE -> actionListener(MediaAction.Pause)
            PLAYER_CONTROL_ACTION_FORWARD -> actionListener(MediaAction.Forward)
            PLAYER_CONTROL_ACTION_BACKWARD -> actionListener(MediaAction.Backward)
            PLAYER_CONTROL_ACTION_STOP -> actionListener(MediaAction.Stop)
        }
    }
}