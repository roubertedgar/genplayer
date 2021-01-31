package com.downstairs.genplayer.content

sealed class MediaAction {
    object Play : MediaAction()
    object Pause : MediaAction()
    object Stop : MediaAction()
    object Forward : MediaAction()
    object Rewind : MediaAction()
    data class SeekTo(val position: Long) : MediaAction()
}