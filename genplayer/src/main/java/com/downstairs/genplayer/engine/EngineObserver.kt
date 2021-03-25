package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.MediaState

abstract class EngineObserver {
    open fun onEngineChanged(engine: PlayerEngine) {}
    open fun onStateChanged(mediaState: MediaState) {}
}