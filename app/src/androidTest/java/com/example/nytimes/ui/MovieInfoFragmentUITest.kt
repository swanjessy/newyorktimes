package com.example.nytimes.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.nytimes.R
import com.example.nytimes.presentation.ui.features.MainActivity
import com.example.nytimes.utils.Constants.ARGS_MOVIE
import com.example.nytimes.utils.TestDataSet
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MovieInfoFragmentUITest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun movieInfoFragmentInflateCorrectViewTest() {
        val movie = TestDataSet.FAKE_MOVIE.results.first()
        val bundle = Bundle()
        bundle.putParcelable(ARGS_MOVIE, movie)

        launchFragment(R.id.movieInfoFragment, bundle)
        onView(withId(R.id.title_movie)).check(matches(withText(movie.headline)))

        onView(withId(R.id.title_movie)).check(
            matches(
                withText(
                    CoreMatchers.containsString(movie.headline)
                )
            )
        )

        onView(withId(R.id.tv_display_title)).check(
            matches(
                withText(
                    CoreMatchers.containsString(movie.display_title)
                )
            )
        )

        onView(withId(R.id.tv_opening_Date)).check(
            matches(
                withText(
                    CoreMatchers.containsString(movie.opening_date)
                )
            )
        )

        onView(withId(R.id.tv_byline)).check(
            matches(
                withText(
                    CoreMatchers.containsString(movie.byline)
                )
            )
        )
    }

    private fun launchFragment(
        destinationId: Int,
        argBundle: Bundle? = null
    ) {
        val launchFragmentIntent = buildLaunchFragmentIntent(destinationId, argBundle)
        activityRule.launchActivity(launchFragmentIntent)
    }

    private fun buildLaunchFragmentIntent(destinationId: Int, argBundle: Bundle?): Intent =
        NavDeepLinkBuilder(InstrumentationRegistry.getInstrumentation().targetContext)
            .setGraph(R.navigation.nav_graph)
            .setComponentName(MainActivity::class.java)
            .setDestination(destinationId)
            .setArguments(argBundle)
            .createTaskStackBuilder().intents[0]
}