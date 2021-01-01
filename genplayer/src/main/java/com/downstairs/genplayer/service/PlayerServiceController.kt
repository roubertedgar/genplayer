package com.downstairs.genplayer.service

import android.content.Context
import com.downstairs.genplayer.SplitPlayer

class PlayerServiceController(context: Context) {

    private val connection = PlayerServiceConnection(context)

    fun connect(onConnected: (SplitPlayer) -> Unit = {}) {
        connection.connect(onConnected)
    }

    fun disconnect() {
        connection.disconnect()
    }
}

