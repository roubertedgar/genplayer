package com.downstairs.genplayer.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.downstairs.genplayer.R
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PAUSE
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PLAY
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.DiscontinuityReason
import com.google.android.exoplayer2.Player.STATE_BUFFERING
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.ui.TimeBar.OnScrubListener
import kotlinx.android.synthetic.main.player_controller_view.view.*

private const val MAX_PROGRESS_DELAY_MS = 1000L
private const val DEFAULT_HIDE_DELAY_MS = 1000L

class PlayerControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val componentListener = ComponentListener()
    private val progressAction: () -> Unit = this::updateProgress
    private val hideDelay: () -> Unit = this::hideController

    private var timeBar: TimeBar? = null
    private var player: Player? = null

    init {
        inflate(context, R.layout.player_controller_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupListeners()
        hideController()
    }

    fun setPlayer(player: Player) {
        player.addListener(componentListener)
        this.player = player
    }

    fun setTimeBar(timeBar: TimeBar) {
        timeBar.addListener(componentListener)
        this.timeBar = timeBar
    }

    private fun hideController() {
        postDelayed(hideDelay, DEFAULT_HIDE_DELAY_MS)
    }

    private fun setupListeners() {
        rewindButton.setOnClickListener { rewind() }
        fastForwardButton.setOnClickListener { seek() }
        playPauseButton.setOnSwitchListener { onSwitchPlayPause(it) }
        fullScreenButton.setOnSwitchListener { onSwitchFullScreen(it) }
    }

    private fun seek() {
        safeSeek(+15000)
    }

    private fun rewind() {
        safeSeek(-15000)
    }

    private fun updateTimeline() {
        timeBar?.setDuration(player?.duration ?: 0)
        updateProgress()
    }


    private fun updateProgress() {
        removeCallbacks(progressAction)
        postDelayed(progressAction, MAX_PROGRESS_DELAY_MS)

        player?.also {
            timeBar?.setPosition(it.currentPosition)
            timeBar?.setBufferedPosition(it.bufferedPosition)
        }
    }

    private fun updatePlayerButton(isPlaying: Boolean) {
        if (isPlaying) {
            playPauseButton.setState(SwitchButton.State.END)
        } else {
            playPauseButton.setState(SwitchButton.State.START)
        }
    }

    private fun onSwitchPlayPause(state: SwitchButton.State) {
        if (state == SwitchButton.State.START) {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PAUSE))
        } else {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PLAY))
        }
    }

    private fun onSwitchFullScreen(state: SwitchButton.State) {
        if (state == SwitchButton.State.START) {
            //exo_progress.showScrubber(300)
        } else {
            // exo_progress.hideScrubber(300)
        }
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

    inner class ComponentListener : Player.EventListener, OnScrubListener {
        //player
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlayerButton(isPlaying)
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            updateTimeline()
        }

        override fun onPositionDiscontinuity(@DiscontinuityReason reason: Int) {
            updateTimeline()
        }
        //endplayer

        //timebar
        override fun onPlaybackStateChanged(state: Int) {
            bufferingSpinProgress.isVisible = state == STATE_BUFFERING
            updateProgress()
        }

        override fun onScrubStart(timeBar: TimeBar, position: Long) {

        }

        override fun onScrubMove(timeBar: TimeBar, position: Long) {

        }

        override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
            if (!canceled) {
                player?.seekTo(position)
            }
        }
    }
}