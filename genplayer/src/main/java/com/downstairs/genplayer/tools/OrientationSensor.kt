package com.downstairs.genplayer.tools

import android.content.Context
import android.hardware.SensorManager
import android.view.OrientationEventListener

class OrientationSensor(private val context: Context) {

    companion object {
        const val THRESHOLD = 25

        const val PORTRAIT_POSITION = 0
        const val INVERSE_PORTRAIT_POSITION = 360

        const val LANDSCAPE_POSITION = 90
        const val INVERSE_LANDSCAPE_POSITION = 270
    }

    private lateinit var lastOrientation: Orientation
    private var onOrientationChanged: (Orientation) -> Unit = {}

    private val orientationListener: OrientationEventListener =
        object : OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
            override fun onOrientationChanged(position: Int) {
                onPositionChanged(position)
            }
        }

    private fun enable() {
        if (orientationListener.canDetectOrientation()) {
            orientationListener.enable()
        }
    }

    fun disable() {
        orientationListener.disable()
    }

    fun setOrientationChangeListener(onChange: (Orientation) -> Unit) {
        onOrientationChanged = onChange
        enable()
    }

    private fun onPositionChanged(position: Int) {
        if (isLandscape(position)) {
            setOrientation(Orientation.LANDSCAPE)
        } else if (isPortrait(position)) {
            setOrientation(Orientation.PORTRAIT)
        }
    }

    private fun setOrientation(orientation: Orientation) {
        if (isLastOrientationNotInitialized() || orientation != lastOrientation) {
            lastOrientation = orientation
            onOrientationChanged(orientation)
        }
    }

    private fun isLandscape(orientation: Int): Boolean {
        val isLandscape =
            orientation <= LANDSCAPE_POSITION + THRESHOLD && orientation >= LANDSCAPE_POSITION - THRESHOLD

        val isInverseLandscape =
            orientation <= INVERSE_LANDSCAPE_POSITION + THRESHOLD && orientation >= INVERSE_LANDSCAPE_POSITION - THRESHOLD

        return isLandscape || isInverseLandscape
    }

    private fun isPortrait(orientation: Int): Boolean {
        val isPortrait = orientation in PORTRAIT_POSITION..THRESHOLD

        val isInversePortrait =
            orientation >= (INVERSE_PORTRAIT_POSITION - THRESHOLD) && orientation <= INVERSE_PORTRAIT_POSITION

        return isPortrait || isInversePortrait
    }

    private fun isLastOrientationNotInitialized() = !(::lastOrientation.isInitialized)
}

enum class Orientation {
    LANDSCAPE,
    PORTRAIT
}