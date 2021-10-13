package com.downstairs.genplayer.view

import com.downstairs.genplayer.tools.orientation.Orientation

sealed class Command {
    object Rewind : Command()
    object Forward : Command()
    data class Seek(val position: Long) : Command()
    data class Playback(val isPlaying: Boolean) : Command()
    data class Rotate(val orientation: Orientation) : Command()
}