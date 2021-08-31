package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.downstairs.genplayer.R
import com.downstairs.genplayer.databinding.MediaButtonsViewBinding
import com.downstairs.genplayer.databinding.MediaControlViewBinding
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.TimeBar

class MediaControl @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val onClickListener: (View) -> Unit = { view -> onButtonClick(view) }

    private var control: MediaControlViewBinding
    private var buttons: MediaButtonsViewBinding

    //private var hideTimer = ViewTimer()
    private var onCommand: (Command) -> Unit = {}

    init {
        inflate(context, R.layout.media_control_view, this)

        control = MediaControlViewBinding.bind(rootView)
        buttons = control.mediaButtons
    }

    fun setListener(onCommand: (Command) -> Unit) {
        this.onCommand = onCommand
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupListeners()
        if (isOnFullScreen()) switchToFullScreen() else switchToPortrait()
    }

    private fun setupListeners() {
        buttons.playback.setOnClickListener(onClickListener)
        buttons.fastForward.setOnClickListener(onClickListener)
        buttons.rewind.setOnClickListener(onClickListener)
        control.orientation.setOnClickListener(onClickListener)

        control.root.setOnClickListener { onRootLayoutClick() }
        control.timeBar.onScrub { progress -> onCommand(Command.Seek(progress)) }
    }

    private fun onButtonClick(view: View) {
        when (view) {
            control.orientation -> emitOrientationCommand()
            buttons.playback -> emitPlaybackCommand()

            buttons.fastForward -> onCommand(Command.Forward)
            buttons.rewind -> onCommand(Command.Rewind)
        }
        showControls()
    }

    private fun onRootLayoutClick() {
        if (buttons.root.isVisible) hideControls() else showControls()
    }

    private fun showControls() {
        buttons.root.isVisible = true
        control.orientation.isVisible = true
        control.timeBar.isVisible = true
        control.timeBar.showScrubber(SCRUBBER_ANIM_DURATION)

       // hideAfterTimeout()
    }

    private fun hideControls() {
        buttons.root.isVisible = false
        control.orientation.isVisible = false
        control.timeBar.hideScrubber(SCRUBBER_ANIM_DURATION)
        if (isOnFullScreen()) control.timeBar.isVisible = false

     //   hideTimer.cancel()
    }

//    private fun hideAfterTimeout() {
//        if (isAttachedToWindow) {
//            hideTimer.schedule(DEFAULT_HIDE_DELAY_MS) { hideControls() }
//        }
//    }

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

     fun updatePlayback(isPlaying: Boolean) {
        if (isPlaying) {
            buttons.playback.state = SwitchButton.State.FINAL
        } else {
            buttons.playback.state = SwitchButton.State.PRIMARY
        }
    }

    fun updateProgress(currentPosition: Long, bufferedPosition: Long, duration: Long) {
        control.timeBar.setPosition(currentPosition)
        control.timeBar.setBufferedPosition(bufferedPosition)
        control.timeBar.setDuration(duration)
    }

    private fun emitPlaybackCommand() {
        val isPlaying = !control.orientation.state.isPrimary
        onCommand(Command.Playback(isPlaying))
    }

    private fun emitOrientationCommand() {
        val isPortrait = control.orientation.state.isPrimary
        onCommand(Command.Rotate(isPortrait))
    }

    private fun isOnFullScreen() = control.orientation.state == SwitchButton.State.FINAL

    companion object {
        private const val DEFAULT_HIDE_DELAY_MS = 5000L
        private const val SCRUBBER_ANIM_DURATION = 300L
    }
}

class ScrubListener(
    val onStart: (Long) -> Unit = {},
    val onMove: (Long) -> Unit = {},
    val onStop: (Long) -> Unit = {}
) : TimeBar.OnScrubListener {
    override fun onScrubStart(timeBar: TimeBar, position: Long) {
        onStart(position)
    }

    override fun onScrubMove(timeBar: TimeBar, position: Long) {
        onMove(position)
    }

    override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
        if (!canceled) {
            onStop(position)
        }
    }
}

fun DefaultTimeBar.onScrub(
    start: (position: Long) -> Unit = {},
    move: (position: Long) -> Unit = {},
    stop: (position: Long) -> Unit = {}
) {
    this.addListener(ScrubListener(start, move, stop))
}

sealed class Command {
    object Rewind : Command()
    object Forward : Command()
    data class Seek(val position: Long) : Command()
    data class Playback(val isPlaying: Boolean) : Command()
    data class Rotate(val isPortrait: Boolean) : Command()
}