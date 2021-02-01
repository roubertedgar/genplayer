package com.downstairs.genplayer.notification

import com.downstairs.genplayer.content.MediaAction

interface MediaActionListener {
    fun onMediaActionReceived(action: MediaAction) {}
}