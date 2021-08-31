package com.downstairs.genplayer.view

import kotlinx.coroutines.*

class ViewTimer {
    private lateinit var job: Job

    fun cancel() {
        if (this::job.isInitialized && job.isActive) {
            job.cancel()
        }
    }

    fun schedule(delayMs: Long, action: () -> Unit) {
        cancel()

        val ioScope = CoroutineScope(Dispatchers.IO)
        val mainScope = CoroutineScope(Dispatchers.Main)

        job = ioScope.launch {
            delay(delayMs)
            mainScope.launch { action() }
        }
    }

    fun repeat(delayMs: Long, action: () -> Unit) {
        repeat(delayMs, 0, action)
    }

    fun repeat(
        delayMs: Long,
        initialDelayMs: Long = 0,
        action: () -> Unit
    ) {
        cancel()

        val ioScope = CoroutineScope(Dispatchers.IO)
        val mainScope = CoroutineScope(Dispatchers.Main)

        job = ioScope.launch {
            delay(initialDelayMs)

            while (true) {
                mainScope.launch { action() }
                delay(delayMs)
            }
        }
    }
}