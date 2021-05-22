package com.downstairs.genplayer

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.downstairs.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
class HomeFragmentComponentTest {

    @Test
    fun open_video_playlist_on_click_at_the_what_videos_button() {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Genplayer)

        onView(withId(R.id.toVideoFragmentButton)).check(matches(isDisplayed()))
    }
}