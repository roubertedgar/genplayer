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
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.ui.TimeBar.OnScrubListener
import kotlinx.android.synthetic.main.player_controller_view.view.*
import kotlinx.coroutines.*

private const val MAX_PROGRESS_UPDATE_MS = 1000L
private const val DEFAULT_HIDE_DELAY_MS = 5000L
private const val DEFAULT_SEEK_TIME_MS = 15000L

class PlayerControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val componentListener = ComponentListener()
    private var progressTimer = ViewTimer(Job())
    private var hideTimer = ViewTimer(Job())

    private var timeBar: DefaultTimeBar? = null
    private var player: Player? = null

    init {
        inflate(context, R.layout.player_controller_view, this)
        isClickable = true
        isFocusable = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupListeners()
    }

    override fun onDetachedFromWindow() {
        hideTimer.cancel()
        progressTimer.cancel()

        super.onDetachedFromWindow()
    }

    override fun performClick(): Boolean {
        hide()
        return super.performClick()
    }

    fun setPlayer(player: Player) {
        player.addListener(componentListener)
        initTimers()
        this.player = player
    }

    fun setTimeBar(timeBar: DefaultTimeBar) {
        timeBar.addListener(componentListener)
        this.timeBar = timeBar

        morphTimeBar()
    }

    private fun morphTimeBar() {
        timeBar?.translationY = context.convertDpToPixel(11f)
        timeBar?.setPadding(
            context.convertDpToPixel(-8f).toInt(), 0,
            context.convertDpToPixel(-8f).toInt(), 0
        )
    }

    fun show() {
        if (!isVisible) {
            showViews()
        }

        hideAfterTimeout()
    }

    fun hide() {
        if (isVisible) {
            hideViews()
            hideTimer.cancel()
        }
    }

    private fun showViews() {
        isVisible = true
        timeBar?.showScrubber(300L)
    }

    private fun hideViews() {
        isVisible = false
        timeBar?.hideScrubber(300L)
    }

    private fun hideAfterTimeout() {
        hideTimer.cancel()

        if (isAttachedToWindow) {
            hideTimer = schedule(DEFAULT_HIDE_DELAY_MS, this::hide)
        }
    }

    private fun setupListeners() {
        rewindButton.setOnClickListener { rewind() }
        fastForwardButton.setOnClickListener { seek() }
        playPauseButton.setOnSwitchListener { onSwitchPlayPause(it) }
        fullScreenButton.setOnSwitchListener { onSwitchFullScreen(it) }
    }

    private fun seek() {
        safeSeek(+DEFAULT_SEEK_TIME_MS)
    }

    private fun rewind() {
        safeSeek(-DEFAULT_SEEK_TIME_MS)
    }

    private fun updateTimeline() {
        timeBar?.setDuration(player?.duration ?: 0)
        updateProgress()
    }


    private fun updateProgress() {
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

        } else {

        }
    }

    private fun initTimers() {
        show()
        progressTimer = repeat(MAX_PROGRESS_UPDATE_MS, this::updateProgress)
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