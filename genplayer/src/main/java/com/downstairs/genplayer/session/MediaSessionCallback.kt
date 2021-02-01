package com.downstairs.genplayer.session

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import com.downstairs.genplayer.content.MediaAction

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