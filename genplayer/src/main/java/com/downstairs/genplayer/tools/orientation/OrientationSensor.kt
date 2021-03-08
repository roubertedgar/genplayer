package com.downstairs.genplayer.tools.orientation

class OrientationSensor(private val orientationListener: OrientationListener) {

    companion object {
        const val THRESHOLD = 30

        const val PORTRAIT_POSITION = 0
        const val INVERSE_PORTRAIT_POSITION = 360

        const val LANDSCAPE_POSITION = 90
        const val INVERSE_LANDSCAPE_POSITION = 270
    }

    private lateinit var lastOrientation: Orientation
    private var onOrientationChanged: (Orientation) -> Unit = {}

    fun setOrientationChangeListener(onChange: (Orientation) -> Unit) {
        onOrientationChanged = onChange
        setupOrientationListener()
    }

    fun disable() {
        orientationListener.disable()
    }

    private fun setupOrientationListener() {
        if (orientationListener.canDetectOrientation()) {
            orientationListener.setOnPositionChangeListener { onPositionChanged(it) }
        }
    }

    private fun onPositionChanged(position: Int) {
        val orientation = when {
            isLandscape(position) -> Orientation.LANDSCAPE
            isPortrait(position) -> Orientation.PORTRAIT
            else -> Orientation.UNKNOWN
        }

        updateOrientation(orientation)
    }

    private fun updateOrientation(orientation: Orientation) {
        if (isLastOrientationNotInitialized() || orientation != lastOrientation) {
            lastOrientation = orientation
            onOrientationChanged(orientation)
        }
    }

    private fun isLandscape(orientation: Int): Boolean {
        val isLandscape = orientation >= LANDSCAPE_POSITION - THRESHOLD
                && orientation <= LANDSCAPE_POSITION + THRESHOLD

        val isInverseLandscape = orientation >= INVERSE_LANDSCAPE_POSITION - THRESHOLD
                && orientation <= INVERSE_LANDSCAPE_POSITION + THRESHOLD

        return isLandscape || isInverseLandscape
    }

    private fun isPortrait(orientation: Int): Boolean {
        val isPortrait = orientation in PORTRAIT_POSITION..THRESHOLD

        val isInversePortrait = orientation >= (INVERSE_PORTRAIT_POSITION - THRESHOLD)
                && orientation <= INVERSE_PORTRAIT_POSITION

        return isPortrait || isInversePortrait
    }

    private fun isLastOrientationNotInitialized() = !(::lastOrientation.isInitialized)
}