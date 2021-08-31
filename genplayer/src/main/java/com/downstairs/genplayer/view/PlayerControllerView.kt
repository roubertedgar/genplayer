package com.downstairs.genplayer.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.FrameLayout
import com.downstairs.genplayer.R
import com.downstairs.genplayer.databinding.PlayerControllerViewBinding
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PAUSE
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PLAY
import com.downstairs.genplayer.tools.orientation.Orientation
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.DiscontinuityReason
import com.google.android.exoplayer2.Timeline

class PlayerControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var listener: ControllerListener = object : ControllerListener {}

    private var binding: PlayerControllerViewBinding
    private var player: Player? = null

 //   private var progressTimer = ViewTimer()

    init {
        inflate(context, R.layout.player_controller_view, this)
        binding = PlayerControllerViewBinding.bind(rootView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) return

        binding.mediaControl.setListener { onCommandChange(it) }
    }

    private fun onCommandChange(command: Command) {
        when (command) {
            is Command.Rotate -> changeOrientation(command.isPortrait)
            is Command.Playback -> playbackChanged(command.isPlaying)
            is Command.Forward -> safeSeek(+DEFAULT_SEEK_TIME_MS)
            is Command.Rewind -> safeSeek(-DEFAULT_SEEK_TIME_MS)
            is Command.Seek -> player?.seekTo(command.position)
        }
    }

    private fun changeOrientation(portrait: Boolean) {
        if (portrait) {
            listener.onOrientationChanged(Orientation.PORTRAIT)
        } else {
            listener.onOrientationChanged(Orientation.LANDSCAPE)
        }
    }

    fun setListener(listener: ControllerListener) {
        this.listener = listener
    }

    fun setPlayer(player: Player) {
        this.player = player
        this.player?.addListener(object : Player.EventListener {

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                updateProgress()
            }

            override fun onPositionDiscontinuity(@DiscontinuityReason reason: Int) {
                updateProgress()
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (player.isPlaying) {
                    //     progressTimer.repeat(MAX_PROGRESS_UPDATE_MS) { updateProgress() }
                    binding.mediaControl.updatePlayback(isPlaying = true)
                } else {
                    binding.mediaControl.updatePlayback(isPlaying = false)
                  //  progressTimer.cancel()
                }
            }
        })
    }

    private fun safeSeek(offsetPosition: Long) {
        player?.also {
            val duration = it.duration
            val position = it.currentPosition + offsetPosition

            val seekPosition = when {
                position < 0 -> 0
                position > duration -> duration
                else -> position
            }

            it.seekTo(seekPosition)
        }
    }

    private fun playbackChanged(isPlaying: Boolean) {
        if (isPlaying) {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PAUSE))
        } else {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PLAY))
        }
    }

    private fun updateProgress() {
        player?.also {
            binding.mediaControl.updateProgress(
                it.currentPosition,
                it.bufferedPosition,
                it.duration
            )
        }
    }

    override fun onDetachedFromWindow() {
     //   progressTimer.cancel()
        super.onDetachedFromWindow()
    }

    fun enable() {}

    fun disable() {}

    fun toPortraitMode() {
        changeOrientation(portrait = true)
    }

    companion object {
        private const val MAX_PROGRESS_UPDATE_MS = 1200L
        private const val DEFAULT_SEEK_TIME_MS = 15000L
    }
}