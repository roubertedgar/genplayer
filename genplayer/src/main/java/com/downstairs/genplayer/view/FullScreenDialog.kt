package com.downstairs.genplayer.view

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.downstairs.genplayer.R
import kotlinx.android.synthetic.main.full_screen_player_dialog.*

class FullScreenDialog(private val ownerActivity: FragmentActivity) :
    Dialog(ownerActivity, android.R.style.Theme_NoTitleBar_Fullscreen) {

    init {
        setContentView(R.layout.full_screen_player_dialog)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        toFullScreen()
    }

    fun show(playerView: PlayerView) {
        playerView.removeParent()
        playerViewContainer.addView(
            playerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        show()
    }

    private fun toFullScreen() {
        ownerActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        window?.decorView.apply {
            this?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }
    }

    private fun exitFullScreen() {
        ownerActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window?.decorView?.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_VISIBLE)
    }
}

fun View.removeParent() {
    (parent as ViewGroup).removeView(this)
}