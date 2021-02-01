package com.downstairs.genplayer.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.session.MediaSessionCallback.Companion.ACTION_STOP

class MediaActionReceiver : BroadcastReceiver() {

    private lateinit var actionListener: MediaActionListener

    fun setMediaActionListener(listener: MediaActionListener) {
        this.actionListener = listener
    }

    override fun onReceive(contxt: Context, intent: Intent) {
        if (sessionCallbackNotInitialized()) return

        when (intent.action) {
            PLAYER_CONTROL_ACTION_PLAY -> actionListener.onMediaActionReceived(MediaAction.Play)
            PLAYER_CONTROL_ACTION_PAUSE -> actionListener.onMediaActionReceived(MediaAction.Pause)
            PLAYER_CONTROL_ACTION_FORWARD -> actionListener.onMediaActionReceived(MediaAction.Forward)
            PLAYER_CONTROL_ACTION_BACKWARD -> actionListener.onMediaActionReceived(MediaAction.Rewind)
            PLAYER_CONTROL_ACTION_STOP -> actionListener.onMediaActionReceived(MediaAction.Stop)
        }
    }

    private fun sessionCallbackNotInitialized() = !::actionListener.isInitialized
}