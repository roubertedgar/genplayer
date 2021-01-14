package com.downstairs.genplayer.engine

import com.downstairs.genplayer.content.MediaStatus

class EngineObservable {

    private val observers = mutableListOf<EngineObserver>()

    fun observe(observer: EngineObserver) {
        observers.add(observer)
    }

    fun removeObservers() {
        observers.clear()
    }

    fun onEngineChanged(engine: PlayerEngine?) {
        for (observer in observers) {
            observer.onEngineChanged(engine!!)
        }
    }

    fun onStateChanged(status: MediaStatus?) {
        for (observer in observers) {
            observer.onStateChanged(status!!)
        }
    }
}