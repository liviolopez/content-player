package com.liviolopez.contentplayer.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.liviolopez.contentplayer.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import androidx.test.core.app.launchActivity
import kotlinx.coroutines.FlowPreview
import org.junit.Before

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @FlowPreview
    @Before
    fun setup() {
        hiltRule.inject()
        val scenario = launchActivity<MainActivity>()
    }

    @Test
    fun is_MainActivity_visible(){
        onView(withId(R.id.activity_main_container)).check(matches(isDisplayed()))
    }
}