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

            setExcludeFromRecents(true)
        }
    }

    override fun onPictureInPictureModeChanged(
        isOnPictureInPicture: Boolean, config: Configuration?
    ) {
        if (!isOnPictureInPicture) {
            pictureInPictureFragment?.exitFromPictureInPicture()
            setExcludeFromRecents(false)
        }
    }
}

val FragmentActivity.currentFragment: Fragment?
    get() = mainFragmentHost.childFragmentManager.fragments.firstOrNull()

fun FragmentActivity.setExcludeFromRecents(exclude: Boolean) {
    val activityManager = getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as? ActivityManager

    if (activityManager != null) {
        val tasks = activityManager.appTasks
        if (!tasks.isNullOrEmpty()) {
            tasks.first().setExcludeFromRecents(exclude)
        }
    }
}
