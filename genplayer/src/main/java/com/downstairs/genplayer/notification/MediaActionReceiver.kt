package com.downstairs.genplayer.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.notification.MediaSessionCallback.Companion.ACTION_STOP
import com.downstairs.genplayer.session.SessionListener

class MediaActionReceiver : BroadcastReceiver() {

    private lateinit var sessionCallback: MediaSessionCallback

    fun setMediaCallback(sessionCallback: MediaSessionCallback) {
        this.sessionCallback = sessionCallback
    }

    override fun onReceive(contxt: Context, intent: Intent) {
        if (sessionCallbackNotInitialized()) return

        when (intent.action) {
            PLAYER_CONTROL_ACTION_PLAY -> sessionCallback.onPlay()
            PLAYER_CONTROL_ACTION_PAUSE -> sessionCallback.onPause()
            PLAYER_CONTROL_ACTION_FORWARD -> sessionCallback.onFastForward()
            PLAYER_CONTROL_ACTION_BACKWARD -> sessionCallback.onRewind()
            PLAYER_CONTROL_ACTION_STOP -> sessionCallback.onCustomAction(ACTION_STOP, Bundle())
        }
    }

    private fun sessionCallbackNotInitialized() = !::sessionCallback.isInitialized
}

class MediaSessionCallback(private val sessionListener: SessionListener) :
    MediaSessionCompat.Callback() {

    companion object {
        const val ACTION_STOP = "stop"
    }

    override fun onPlay() {
        sessionListener.onMediaActionReceived(MediaAction.Play)
    }

    override fun onPause() {
        sessionListener.onMediaActionReceived(MediaAction.Pause)
    }

    override fun onStop() {
        sessionListener.onMediaActionReceived(MediaAction.Stop)
    }

    override fun onFastForward() {
        sessionListener.onMediaActionReceived(MediaAction.Forward)
    }

    override fun onRewind() {
        sessionListener.onMediaActionReceived(MediaAction.Rewind)
    }

    override fun onSeekTo(position: Long) {
        sessionListener.onMediaActionReceived(MediaAction.SeekTo(position))
    }

    override fun onCustomAction(action: String?, extras: Bundle?) {
        if (action == ACTION_STOP) {
            sessionListener.onMediaActionReceived(MediaAction.Stop)
        }
    }
}