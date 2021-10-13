package com.downstairs.genplayer.view.control

import com.google.android.exoplayer2.ui.TimeBar

class ScrubListener(
    val onStart: (Long) -> Unit = {},
    val onMove: (Long) -> Unit = {},
    val onStop: (Long) -> Unit = {}
) : TimeBar.OnScrubListener {
    override fun onScrubStart(timeBar: TimeBar, position: Long) {
        onStart(position)
    }

    override fun onScrubMove(timeBar: TimeBar, position: Long) {
        onMove(position)
    }

    override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
        if (!canceled) {
            onStop(position)
        }
    }
}