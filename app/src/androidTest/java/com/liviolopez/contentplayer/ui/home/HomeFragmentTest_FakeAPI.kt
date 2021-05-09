package com.liviolopez.contentplayer.ui.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import com.liviolopez.contentplayer.R
import com.liviolopez.contentplayer._components.launchFragmentInHiltContainer
import com.liviolopez.contentplayer.ui._components.AppFragmentFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.FlowPreview
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@FlowPreview
@SmallTest
@HiltAndroidTest
class HomeFragmentTest_FakeAPI{

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        hiltRule.inject()

        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        launchFragmentInHiltContainer<HomeFragment>(factory = fragmentFactory)
    }

    @After
    fun tearDown() = mockWebServer.shutdown()

    @Test
    fun is_OverlayStandByView_on_initial_status(){
        onView(withId(R.id.progress_bar_container)).check(ViewAssertions.matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.error_container)).check(ViewAssertions.matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.empty_list)).check(ViewAssertions.matches(withEffectiveVisibility(Visibility.GONE)))
    }
}