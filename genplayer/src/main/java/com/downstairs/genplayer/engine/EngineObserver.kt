package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.MediaState

interface EngineObserver {
    fun onEngineChanged(engine: PlayerEngine) {}
    fun onStateChanged(mediaState: MediaState) {}
}