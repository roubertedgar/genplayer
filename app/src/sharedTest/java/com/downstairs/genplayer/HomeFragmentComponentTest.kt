package com.downstairs.genplayer

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentComponentTest {

    private val navController =
        TestNavHostController(ApplicationProvider.getApplicationContext())

    private val scenario =
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Genplayer)

    @Before
    fun setUp() {
        scenario.withFragment {
            navController.setGraph(R.navigation.main_nav_graph)
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun open_video_playlist_on_click_at_the_what_videos_button() {
        onView(withId(R.id.toVideoFragmentButton)).perform(click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.videoFragment)

    }
}