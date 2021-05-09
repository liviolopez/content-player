package com.liviolopez.contentplayer.other

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.liviolopez.contentplayer.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Run this test with Light and Dark mode
 */
@RunWith(AndroidJUnit4::class)
class ThemeColorTest {

    @Test
    fun theme_LIGHT_primaryColor_return_true_with_ff6200ee() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val mode = context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        val color = "#ff6200ee"
        @SuppressLint("ResourceType")
        val colorAsString:String = context.getString(R.color.primary)

        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Assert.assertNotEquals(color, colorAsString)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Assert.assertEquals(color, colorAsString)
            }
        }
    }

    @Test
    fun theme_DARK_primaryColor_return_true_with_ff2a005f() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val mode = context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        val color = "#ff2a005f"
        @SuppressLint("ResourceType")
        val colorAsString:String = context.getString(R.color.primary)

        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Assert.assertEquals(color, colorAsString)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Assert.assertNotEquals(color, colorAsString)
            }
        }
    }
}