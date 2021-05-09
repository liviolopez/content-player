package com.liviolopez.contentplayer

import com.liviolopez.contentplayer.other.StringUtilsTest
import com.liviolopez.contentplayer.other.ThemeColorTest
import com.liviolopez.contentplayer.ui.MainActivityTest
import com.liviolopez.contentplayer.ui.home.HomeFragmentTest_FakeAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.runner.RunWith
import org.junit.runners.Suite

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    HomeFragmentTest_FakeAPI::class,
    StringUtilsTest::class,
    ThemeColorTest::class
)
class AppTestSuite