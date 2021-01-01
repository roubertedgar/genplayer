package com.downstairs.genplayer.view

import com.downstairs.genplayer.engine.PlayerEngine
import com.google.android.exoplayer2.ui.PlayerView

fun PlayerView.setPlayer(playerEngine: PlayerEngine) {
    this.player = playerEngine.player
}