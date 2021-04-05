package com.downstairs.genplayer

import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.engine.*
import com.downstairs.genplayer.playlist.ContentChangeListener
import com.downstairs.genplayer.playlist.Playlist
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import javax.inject.Inject

class GenPlayer @Inject constructor(
    private val deviceEngine: DeviceEngine,
    private val castEngine: CastEngine
) {

    private var currentEngine: PlayerEngine = deviceEngine
    private val engineObservable = EngineObservable()
    private val playList = Playlist()

    init {
        setEngineObservable()
        setSessionListener()
        setOnContentChangeListener()
        switchEngine(if (castEngine.isCastSessionAvailable()) castEngine else deviceEngine)

    }

    fun addContent(content: Content) {
        playList.set(content)
    }

    fun addEngineListener(engineObserver: EngineObserver) {
        engineObservable.observe(engineObserver)
        engineObservable.onEngineChanged(currentEngine)
    }

    fun play() {
        currentEngine.play()
    }

    fun pause() {
        currentEngine.pause()
    }

    fun forward() {
        currentEngine.forward()
    }

    fun rewind() {
        currentEngine.rewind()
    }

    fun seekTo(position: Long) {
        currentEngine.seekTo(position)
    }

    fun release() {
        engineObservable.removeObservers()
        castEngine.release()
        deviceEngine.release()
    }

    private fun setEngineObservable() {
        deviceEngine.setObservable(engineObservable)
        castEngine.setObservable(engineObservable)
    }

    private fun setSessionListener() {
        castEngine.setSessionChangeListener(object : SessionAvailabilityListener {
            override fun onCastSessionAvailable() {
                switchEngine(castEngine)
            }

            override fun onCastSessionUnavailable() {
                switchEngine(deviceEngine)
            }
        })
    }

    private fun setOnContentChangeListener() {
        playList.addContentChangeListener(ContentChangeListener { content ->
            currentEngine.prepare(content)
        })
    }

    private fun switchEngine(engine: PlayerEngine) {
        if (currentEngine != engine) {
            currentEngine.release()
            currentEngine = engine
            playList.current()

            engineObservable.onEngineChanged(currentEngine)
        }
    }
}
