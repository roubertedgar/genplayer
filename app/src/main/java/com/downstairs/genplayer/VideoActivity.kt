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

    private val pictureInPictureFragment: PictureInPictureFragment?
        get() = currentFragment as? PictureInPictureFragment

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUserLeaveHint() {
        pictureInPictureFragment?.also { enterOnPictureInPictureMode(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun enterOnPictureInPictureMode(fragment: PictureInPictureFragment) {
        if (fragment.wishEnterOnPipMode) {
            fragment.enterOnPictureInPicture()

            enterPictureInPictureMode(
                PictureInPictureParams.Builder().build()
            )
        }
    }

    override fun onPictureInPictureModeChanged(
        isOnPictureInPicture: Boolean, config: Configuration?
    ) {
        if (!isOnPictureInPicture) {
            pictureInPictureFragment?.exitFromPictureInPicture()
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