package com.downstairs.genplayer.session

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.notification.*
import javax.inject.Inject

class MediaSessionReceiver
@Inject constructor(private val context: Context) : MediaSessionCompat.Callback() {

    private var onActionReceived: (MediaAction) -> Unit = {}

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context, intent: Intent) {
            when (intent.action) {
                PLAYER_CONTROL_ACTION_PLAY -> onActionReceived(MediaAction.Play)
                PLAYER_CONTROL_ACTION_PAUSE -> onActionReceived(MediaAction.Pause)
                PLAYER_CONTROL_ACTION_FORWARD -> onActionReceived(MediaAction.Forward)
                PLAYER_CONTROL_ACTION_BACKWARD -> onActionReceived(MediaAction.Rewind)
                PLAYER_CONTROL_ACTION_STOP -> onActionReceived(MediaAction.Stop)
            }
        }
    }

    init {
        MediaNotificationAction.values().forEach { action ->
            context.registerReceiver(receiver, IntentFilter(action.filter))
        }
    }

    fun onActionReceived(onActionReceived: (MediaAction) -> Unit) {
        this.onActionReceived = onActionReceived
    }

    override fun onPlay() {
        onActionReceived(MediaAction.Play)
    }

    override fun onPause() {
        onActionReceived(MediaAction.Pause)
    }

    override fun onStop() {
        onActionReceived(MediaAction.Stop)
    }

    override fun onFastForward() {
        onActionReceived(MediaAction.Forward)
    }

    override fun onRewind() {
        onActionReceived(MediaAction.Rewind)
    }

    override fun onSeekTo(position: Long) {
        onActionReceived(MediaAction.SeekTo(position))
    }

    override fun onCustomAction(action: String?, extras: Bundle?) {
        if (action == ACTION_STOP) {
            onActionReceived(MediaAction.Stop)
        }
    }

    fun release() {
        context.unregisterReceiver(receiver)
    }

    companion object {
        const val ACTION_STOP = "stop"
    }
}