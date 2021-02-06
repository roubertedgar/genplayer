package com.downstairs.genplayer.view

import kotlinx.coroutines.*

class ViewTimer(private val job: Job) {
    fun cancel() {
        if (job.isActive) {
            job.cancel()
        }
    }
}

inline fun schedule(delayMs: Long, crossinline action: () -> Unit): ViewTimer {
    val defaultScope = CoroutineScope(Dispatchers.Default)
    val mainScope = CoroutineScope(Dispatchers.Main)

    return ViewTimer(defaultScope.launch {
        delay(delayMs)
        mainScope.launch { action() }
    })
}

inline fun repeat(delayMs: Long, crossinline action: () -> Unit) = repeat(delayMs, 0, action)

inline fun repeat(
    delayMs: Long,
    initialDelayMs: Long = 0,
    crossinline action: () -> Unit
): ViewTimer {
    val defaultScope = CoroutineScope(Dispatchers.Default)
    val mainScope = CoroutineScope(Dispatchers.Main)

    return ViewTimer(defaultScope.launch {
        delay(initialDelayMs)

        while (true) {
            mainScope.launch { action() }
            delay(delayMs)
        }
    })
}
