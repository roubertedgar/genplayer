package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.MediaStatus

interface EngineObserver {
    fun onEngineChanged(engine: PlayerEngine) {}
    fun onStateChanged(mediaStatus: MediaStatus) {}
}