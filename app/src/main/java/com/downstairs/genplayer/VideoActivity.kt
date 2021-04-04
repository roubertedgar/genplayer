package com.downstairs.genplayer

import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity(R.layout.video_activity) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUserLeaveHint() {
        enterPictureInPictureMode(
            PictureInPictureParams
                .Builder()
                .build()
        )
    }
}