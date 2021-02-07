package com.downstairs.genplayer.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import com.downstairs.genplayer.R
import kotlinx.android.synthetic.main.full_screen_player_dialog.playerViewFullScreenContainer
import kotlinx.android.synthetic.main.player_view_surface.*

class FullScreenDialog(context: Context) :
    Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {

    private var playerViewSurface: PlayerViewSurface? = null

    init {
        setContentView(R.layout.full_screen_player_dialog)

        (context as? Activity)?.also {
            setOwnerActivity(it)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        toFullScreen()

        playerViewSurface?.removeParent()

        playerViewFullScreenContainer.addView(
            playerViewSurface,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        playerViewController?.toFullScreenMode()
    }

    fun show(playerViewSurface: PlayerViewSurface) {
        if (ownerActivity == null) return

        this.playerViewSurface = playerViewSurface
        show()
    }

    override fun dismiss() {
        exitFullScreen()
        super.dismiss()
    }

    private fun toFullScreen() {
        ownerActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

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
        ownerActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window?.decorView?.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_VISIBLE)

        playerViewController?.toPortraitMode()
        playerViewSurface?.removeParent()
    }

    override fun onBackPressed() {
        playerViewController?.toPortraitMode()
    }
}

fun View.removeParent() {
    (parent as ViewGroup).removeView(this)
}