package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.content.MediaAction
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import javax.inject.Inject

class EngineManager @Inject constructor(
    private val deviceEngine: DeviceEngine,
    private val castEngine: CastEngine
) {

    private var currentEngine: PlayerEngine = deviceEngine
    private val engineObservable = EngineObservable()

    init {
        setEngineObservable()
        setSessionListener()
        switchEngine(if (castEngine.isCastSessionAvailable()) castEngine else deviceEngine)
    }

    private fun setEngineObservable() {
        deviceEngine.setObservable(engineObservable)
        castEngine.setObservable(engineObservable)
    }

    fun observe(observer: EngineObserver) {
        engineObservable.observe(observer)
        engineObservable.onEngineChanged(currentEngine)
    }

    fun prepare(content: Content) {
        currentEngine.prepare(content)
    }

    fun performAction(action: MediaAction) {
        currentEngine.perform(action)
    }

    fun release() {
        engineObservable.removeObservers()
        castEngine.release()
        deviceEngine.release()
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

    private fun switchEngine(engine: PlayerEngine) {
        if (currentEngine != engine) {
            currentEngine.perform(MediaAction.Stop)
            currentEngine = engine
            engineObservable.onEngineChanged(currentEngine)
        }
    }
}