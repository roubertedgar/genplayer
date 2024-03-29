package com.downstairs.genplayer.view.control

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.downstairs.genplayer.R
import com.downstairs.genplayer.databinding.MediaControlViewBinding
import com.downstairs.genplayer.tools.orientation.Orientation
import com.downstairs.genplayer.view.Command
import com.downstairs.genplayer.view.SwitchButton
import com.downstairs.genplayer.view.ViewTimer
import com.downstairs.genplayer.view.components.PlaybackState
import com.downstairs.genplayer.view.horizontalPadding
import com.downstairs.genplayer.view.isScrubVisible
import com.downstairs.genplayer.view.moveY
import com.downstairs.genplayer.view.onScrub

class MediaControl @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var control: MediaControlViewBinding

    private var hideTimer = ViewTimer()
    private var onCommand: (Command) -> Unit = {}

    init {
        inflate(context, R.layout.media_control_view, this)
        control = MediaControlViewBinding.bind(rootView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupListeners()
        if (isInPortrait) switchToPortrait() else switchToFullScreen()
    }

    private val onClickListener: (View) -> Unit = { view ->
        when (view) {
            control.fastForward -> onCommand(Command.Forward)
            control.rewind -> onCommand(Command.Rewind)
        }

        setControlVisibility(isVisible = true)
    }

    private fun setupListeners() {
        control.playback.setOnClickListener(onClickListener)
        control.fastForward.setOnClickListener(onClickListener)
        control.rewind.setOnClickListener(onClickListener)
        control.orientation.setOnClickListener(onClickListener)

        control.playback.setOnStateChangeListener { playbackSwitched(it) }
        control.orientation.setOnSwitchListener { orientationSwitched(it) }

        control.root.setOnClickListener { setControlVisibility(isVisible = !isControlVisible) }
        control.timeBar.onScrub { progress -> onCommand(Command.Seek(progress)) }
    }

    fun setListener(onCommand: (Command) -> Unit) {
        this.onCommand = onCommand
    }

    fun setPlaybackState(state: PlaybackState) {
        control.playback.state = state
    }

    fun updateProgress(currentPosition: Long, bufferedPosition: Long, duration: Long) {
        control.timeBar.setPosition(currentPosition)
        control.timeBar.setBufferedPosition(bufferedPosition)
        control.timeBar.setDuration(duration)
    }

    private fun setControlVisibility(isVisible: Boolean) {
        val isTimeBarVisible = if (isInPortrait) true else isVisible
        val frameVisibility = if (isVisible) VISIBLE else INVISIBLE
        if (isVisible) hideAfterTimeout() else hideTimer.cancel()

        control.frame.visibility = frameVisibility
        control.playback.isVisible = isVisible
        control.fastForward.isVisible = isVisible
        control.rewind.isVisible = isVisible
        control.orientation.isVisible = isVisible
        control.timeBar.isVisible = isTimeBarVisible
        control.timeBar.isScrubVisible = isVisible
    }

    private fun hideAfterTimeout() {
        if (isAttachedToWindow) {
            hideTimer.schedule(DEFAULT_HIDE_DELAY_MS) { setControlVisibility(false) }
        }
    }

    private fun switchToFullScreen() {
        control.bottomContainer.moveY(0f)
        control.timeBar.horizontalPadding(16f)
        control.timeBarPlaceHolder.isVisible = false
    }

    private fun switchToPortrait() {
        control.bottomContainer.moveY(11f)
        control.timeBar.horizontalPadding(-8f)
        control.timeBarPlaceHolder.isVisible = true
    }

    private fun playbackSwitched(state: PlaybackState) {
        if (state == PlaybackState.Playing) {
            onCommand(Command.Playback(isPlaying = true))
        } else if (state == PlaybackState.Paused) {
            onCommand(Command.Playback(isPlaying = false))
        }
    }

    private fun orientationSwitched(state: SwitchButton.State) {
        if (state == SwitchButton.State.PRIMARY) {
            onCommand(Command.Rotate(Orientation.PORTRAIT))
        } else {
            onCommand(Command.Rotate(Orientation.LANDSCAPE))
        }
    }

    private val isInPortrait get() = control.orientation.state == SwitchButton.State.PRIMARY
    private val isControlVisible get() = control.frame.isVisible

    companion object {
        private const val DEFAULT_HIDE_DELAY_MS = 5000L
    }
}
