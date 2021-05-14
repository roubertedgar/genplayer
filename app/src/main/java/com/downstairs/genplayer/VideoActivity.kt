package com.downstairs.genplayer

import android.app.ActivityManager
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

            removeFromResents(true)
        }
    }

    private fun removeFromResents(remove: Boolean) {
        val am = getSystemService(ACTIVITY_SERVICE) as? ActivityManager
        if (am != null) {
            val tasks = am.appTasks
            if (tasks != null && tasks.size > 0) {
                tasks[0].setExcludeFromRecents(remove)
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isOnPictureInPicture: Boolean, config: Configuration?
    ) {
        if (!isOnPictureInPicture) {
            pictureInPictureFragment?.exitFromPictureInPicture()
            removeFromResents(false)
        }
    }
}

val FragmentActivity.currentFragment: Fragment?
    get() = mainFragmentHost.childFragmentManager.fragments.firstOrNull()