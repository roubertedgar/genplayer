package com.downstairs.genplayer

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.video_activity.*

class VideoActivity : AppCompatActivity(R.layout.video_activity) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUserLeaveHint() {
        currentFragment?.also { fragment ->
            val wishEnterOnPip =
                (fragment as? PictureInPictureFragment)?.wishEnterOnPipMode ?: false

            if (wishEnterOnPip) {
                enterPictureInPictureMode(PictureInPictureParams.Builder().build())
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, newConfig: Configuration?
    ) {
        currentFragment?.also { fragment ->
            fragment.onPictureInPictureModeChanged(isInPictureInPictureMode)
        }
    }
}

val FragmentActivity.currentFragment: Fragment?
    get() {
        return if (mainFragmentHost.childFragmentManager.fragments.isNotEmpty()) {
            mainFragmentHost.childFragmentManager.fragments[0]
        } else {
            null
        }
    }