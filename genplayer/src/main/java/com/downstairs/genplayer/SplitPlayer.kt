package com.downstairs.genplayer

import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.content.MediaAction
import com.downstairs.genplayer.engine.EngineManager
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.engine.PlayerEngine
import com.downstairs.genplayer.playlist.ContentChangeListener
import com.downstairs.genplayer.playlist.Playlist
import javax.inject.Inject

class SplitPlayer @Inject constructor(private val engineManager: EngineManager) {

    private val playList = Playlist()

    fun addContent(content: Content) {
        playList.set(content)
    }

    fun addEngineListener(engineObserver: EngineObserver) {
        engineManager.observe(engineObserver)
    }

    init {
        playList.addContentChangeListener(ContentChangeListener { content ->
            engineManager.prepare(content)
        })

        engineManager.observe(object : EngineObserver {
            override fun onEngineChanged(engine: PlayerEngine) {
                playList.current()
            }
        })
    }

    fun performAction(action: MediaAction) {
        engineManager.performAction(action)
    }

    fun release() {
        engineManager.release()
    }
}
