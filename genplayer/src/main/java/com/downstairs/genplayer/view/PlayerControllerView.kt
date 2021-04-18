package com.downstairs.genplayer.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.downstairs.genplayer.R
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PAUSE
import com.downstairs.genplayer.notification.PLAYER_CONTROL_ACTION_PLAY
import com.downstairs.genplayer.tools.orientation.Orientation
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.DiscontinuityReason
import com.google.android.exoplayer2.Player.STATE_BUFFERING
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.ui.TimeBar.OnScrubListener
import kotlinx.android.synthetic.main.player_controller_view.view.*
import kotlinx.android.synthetic.main.player_main_control_buttons_layout.view.*

class PlayerControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var listener: ControllerListener = object : ControllerListener {}
    private val componentListener = ComponentListener()

    private var progressTimer = ViewTimer()
    private var hideTimer = ViewTimer()

    private var player: Player? = null

    init {
        inflate(context, R.layout.player_controller_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setupListeners()
        setupViewOrientation()
        initTimers()
    }

    fun setListener(listener: ControllerListener) {
        this.listener = listener
    }

    fun setPlayer(player: Player) {
        this.player = player
        this.player?.addListener(componentListener)

        updateTimeline()
    }

    fun toPortraitMode() {
        fullScreenButton.state = SwitchButton.State.INITIAL
    }

    fun toFullScreenMode() {
        fullScreenButton.state = SwitchButton.State.FINAL
    }

    fun disable() {
        isVisible = false
    }

    fun enable() {
        isVisible = true
    }

    private fun setupListeners() {
        fastForwardButton.setOnClickListener { safeSeek(+DEFAULT_SEEK_TIME_MS) }
        rewindButton.setOnClickListener { safeSeek(-DEFAULT_SEEK_TIME_MS) }

        playButton.setOnSwitchListener { onSwitchPlayPause(it) }
        fullScreenButton.setOnSwitchListener { onSwitchFullScreen(it) }

        playerTimeBar.addListener(componentListener)

        rootControlsContainer.setOnClickListener {
            if (playerButtonsContainer.isVisible) hideControls() else showControls()
        }
    }

    private fun setupViewOrientation() {
        if (isOnFullScreen()) switchToFullScreen() else switchToPortrait()
    }

    private fun initTimers() {
        cancelTimers()

        showControls()
        progressTimer.repeat(MAX_PROGRESS_UPDATE_MS, this::updateProgress)
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

    private fun updatePlayButtonState(isPlaying: Boolean) {
        if (isPlaying) {
            playButton.state = SwitchButton.State.FINAL
        } else {
            playButton.state = SwitchButton.State.INITIAL
        }
    }

    private fun onSwitchPlayPause(state: SwitchButton.State) {
        if (state == SwitchButton.State.INITIAL) {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PAUSE))
        } else {
            context.sendBroadcast(Intent(PLAYER_CONTROL_ACTION_PLAY))
        }
    }

    private fun onSwitchFullScreen(state: SwitchButton.State) {
        if (state == SwitchButton.State.INITIAL) {
            listener.onOrientationChanged(Orientation.PORTRAIT)
            switchToPortrait()
        } else {
            listener.onOrientationChanged(Orientation.LANDSCAPE)
            switchToFullScreen()
        }
    }

    private fun switchToFullScreen() {
        bottomBarContainer.moveY(0f)
        playerTimeBar.horizontalPadding(16f)
        timelinePlaceholder.isVisible = false
    }

    private fun switchToPortrait() {
        bottomBarContainer.moveY(11f)
        playerTimeBar.horizontalPadding(-8f)
        timelinePlaceholder.isVisible = true
    }

    //visibility ====================================
    private fun showControls() {
        if (!isVisible()) {
            playerButtonsContainer.isVisible = true
            fullScreenButton.isVisible = true
            showTimeBar()
        }

        hideAfterTimeout()
    }

    private fun hideControls() {
        if (isVisible()) {
            playerButtonsContainer.isVisible = false
            fullScreenButton.isVisible = false
            hideTimeBar()
            hideTimer.cancel()
        }
    }

    private fun showTimeBar() {
        playerTimeBar.showScrubber(SCRUBBER_ANIM_DURATION)
        playerTimeBar.isVisible = true
    }

    private fun hideTimeBar() {
        playerTimeBar.hideScrubber(SCRUBBER_ANIM_DURATION)
        if (isOnFullScreen()) {
            playerTimeBar.isVisible = false
        }
    }

    private fun hideAfterTimeout() {
        if (isAttachedToWindow) {
            hideTimer.schedule(DEFAULT_HIDE_DELAY_MS, this::hideControls)
        }
    }
    //end visibility ===============================================

    private fun updateTimeline() {
        player?.also { playerTimeBar.setDuration(it.duration) }
    }

    private fun updateProgress() {
        player?.also {
            playerTimeBar.setPosition(it.currentPosition)
            playerTimeBar.setBufferedPosition(it.bufferedPosition)
        }
    }

    private fun isVisible() = playerButtonsContainer.isVisible

    private fun isOnFullScreen() = fullScreenButton.state == SwitchButton.State.FINAL

    override fun onDetachedFromWindow() {
        cancelTimers()
        super.onDetachedFromWindow()
    }

    private fun cancelTimers() {
        hideTimer.cancel()
        progressTimer.cancel()
    }

    companion object {
        private const val MAX_PROGRESS_UPDATE_MS = 1000L
        private const val DEFAULT_HIDE_DELAY_MS = 5000L
        private const val DEFAULT_SEEK_TIME_MS = 15000L
        private const val SCRUBBER_ANIM_DURATION = 300L
    }

    inner class ComponentListener : Player.EventListener, OnScrubListener {

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            updateTimeline()
        }

        override fun onPositionDiscontinuity(@DiscontinuityReason reason: Int) {
            updateTimeline()
        }

        override fun onPlaybackStateChanged(state: Int) {
            val isBuffering = state == STATE_BUFFERING
            bufferingSpinProgress.isVisible = isBuffering
            updatePlayButtonState(!isBuffering)
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