package com.downstairs.genplayer.view

import com.downstairs.genplayer.tools.Orientation

interface ControllerListener {

    fun onHide() {}

    fun onShow() {}

    fun onOrientationRequested(orientation: Orientation) {}

    fun onOrientationChanged(orientation: Orientation) {}
}