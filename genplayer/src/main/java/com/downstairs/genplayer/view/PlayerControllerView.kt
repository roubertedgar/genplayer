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

    companion object {
        private const val MAX_PROGRESS_UPDATE_MS = 1000L
        private const val DEFAULT_HIDE_DELAY_MS = 5000L
        private const val DEFAULT_SEEK_TIME_MS = 15000L
    }

    init {
        inflate(context, R.layout.player_controller_view, this)

        isClickable = true
        isFocusable = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setupListeners()
        setupViewOrientation()
        initTimers()
    }

    override fun onDetachedFromWindow() {
        cancelTimers()
        super.onDetachedFromWindow()
    }

    override fun performClick(): Boolean {
        if (buttonsContainer.isVisible) {
            hide()
        } else {
            show()
        }

        return super.performClick()
    }

    fun setListener(listener: ControllerListener) {
        this.listener = listener
    }

    fun setPlayer(player: Player) {
        this.player = player
        this.player?.addListener(componentListener)

        updateTimeline()
    }

    private fun show() {
        if (!isVisible()) {
            showViews()
        }

        hideAfterTimeout()
    }

    private fun hide() {
        if (isVisible()) {
            hideViews()
            hideTimer.cancel()
        }
    }

    private fun showViews() {
        buttonsContainer.isVisible = true
        playerTimeBar.show()
    }

    private fun hideViews() {
        buttonsContainer.isVisible = false
        playerTimeBar.hide(isOnFullScreen())
    }

    private fun hideAfterTimeout() {
        if (isAttachedToWindow) {
            hideTimer.schedule(DEFAULT_HIDE_DELAY_MS, this::hide)
        }
    }

    private fun setupListeners() {
        rewindButton.setOnClickListener { rewind() }
        fastForwardButton.setOnClickListener { seek() }
        playPauseButton.setOnSwitchListener { onSwitchPlayPause(it) }
        fullScreenButton.setOnSwitchListener { onSwitchFullScreen(it) }

        playerTimeBar.addListener(componentListener)
    }

    private fun setupViewOrientation() {
        if (isOnFullScreen()) {
            toFullScreenMode()
        } else {
            toPortraitMode()
        }
    }

    private fun seek() {
        safeSeek(+DEFAULT_SEEK_TIME_MS)
    }

    private fun rewind() {
        safeSeek(-DEFAULT_SEEK_TIME_MS)
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

    private fun updateTimeline() {
        player?.also { playerTimeBar.setDuration(it.duration) }
    }

    private fun updateProgress() {
        player?.also {
            playerTimeBar.setPosition(it.currentPosition)
            playerTimeBar.setBufferedPosition(it.bufferedPosition)
        }
    }

    private fun updatePlayerButton(isPlaying: Boolean) {
        if (isPlaying) {
            playPauseButton.state = SwitchButton.State.FINAL
        } else {
            playPauseButton.state = SwitchButton.State.INITIAL
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
            listener.onOrientationRequested(Orientation.PORTRAIT)
        } else {
            listener.onOrientationRequested(Orientation.LANDSCAPE)
        }
    }

    fun toFullScreenMode() {
        listener.onOrientationChanged(Orientation.LANDSCAPE)
        playerTimeBar.toFullScreenConstraints()
        buttonsContainer.setBottomMargin(0f)
    }

    fun toPortraitMode() {
        listener.onOrientationChanged(Orientation.PORTRAIT)
        playerTimeBar.toPortraitConstraints()
        buttonsContainer.setBottomMargin(playerTimeBar.barTopHeight)
    }

    private fun initTimers() {
        cancelTimers()

        show()
        progressTimer.repeat(MAX_PROGRESS_UPDATE_MS, this::updateProgress)
    }

    private fun isVisible() = buttonsContainer.isVisible

    private fun isOnFullScreen() = fullScreenButton.state == SwitchButton.State.FINAL

    private fun cancelTimers() {
        hideTimer.cancel()
        progressTimer.cancel()
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