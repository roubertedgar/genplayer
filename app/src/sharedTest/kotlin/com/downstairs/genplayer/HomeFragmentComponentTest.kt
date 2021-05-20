package com.downstairs.genplayer

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.downstairs.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28], application = TestApplication::class)
class HomeFragmentComponentTest {

    @Test
    fun open_video_playlist_on_click_at_the_what_videos_button() {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Genplayer)
    }
}