package com.downstairs.genplayer.tools.orientation

import android.content.Context
import android.view.OrientationEventListener

class OrientationListener(context: Context) : OrientationEventListener(context) {

    private var onPositionChanged: (Int) -> Unit = {}

    fun setOnPositionChangeListener(onPositionChanged: (Int) -> Unit) {
        this.onPositionChanged = onPositionChanged
        this.enable()
    }

    override fun onOrientationChanged(orientation: Int) {
        onPositionChanged(orientation)
    }
}