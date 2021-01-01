package com.downstairs.genplayer.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PAUSE
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PLAY
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import kotlinx.android.synthetic.main.player_controller_view.view.*

class PlayerControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    playbackAttrs: AttributeSet? = attrs
) : PlayerControlView(context, attrs, defStyleAttr, playbackAttrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupListeners()
    }

    override fun setPlayer(player: Player?) {
        super.setPlayer(player)

        if (player != null) {
            changePlayPauseButtonState(player.isPlaying)

            player.addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    changePlayPauseButtonState(isPlaying)
                }
            })
        }
    }

    private fun setupListeners() {
        playPauseButton.setOnClickListener {
            onPlayPauseClick()
        }
    }

    private fun onPlayPauseClick() {
        if (playPauseButton.isChecked) {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PAUSE))
        } else {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PLAY))
        }
    }

    private fun changePlayPauseButtonState(isPlaying: Boolean) {
        if (isPlaying) {
            playPauseButton.moveToState(AnimatedVectorButton.State.STATE_END)
        } else {
            playPauseButton.moveToState(AnimatedVectorButton.State.STATE_START)
        }
    }
}