package com.downstairs.genplayer

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.downstairs.TestApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentComponentTest {

    @Test
    fun open_video_playlist_on_click_at_the_what_videos_button() {
        val navController =
            TestNavHostController(ApplicationProvider.getApplicationContext<TestApplication>())

        val scenario = launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Genplayer)
        scenario.onFragment(object: FragmentScenario.FragmentAction<HomeFragment> {
            override fun perform(fragment: HomeFragment) {
                navController.setGraph(R.navigation.main_nav_graph)
                Navigation.setViewNavController(fragment.requireView(), navController)
            }
        })

        onView(withId(R.id.toVideoFragmentButton)).perform(click())

        assertThat(navController.currentDestination?.displayName).isEqualTo(R.id.videoFragment)

    }
}