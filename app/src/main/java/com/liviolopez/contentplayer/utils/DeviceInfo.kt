package com.liviolopez.contentplayer.utils

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.telephony.TelephonyManager
import java.util.*
import javax.inject.Inject

class DeviceInfo @Inject constructor(val context: Context) {
    val isTv = (context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager).currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    val isPhone = Objects.requireNonNull(context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).phoneType != TelephonyManager.PHONE_TYPE_NONE

    enum class ScreenSize { Phone, Tablet, TabletXL }
    private val smallScreenDp = context.resources.configuration.smallestScreenWidthDp
    val screenSize =  when{
        smallScreenDp >= 720  -> { ScreenSize.TabletXL }
        smallScreenDp >= 600  -> { ScreenSize.Tablet }
        else -> { ScreenSize.Phone }
    }

    val inPortrait = context.resources.displayMetrics.widthPixels < context.resources.displayMetrics.heightPixels
    val inLandscape = !inPortrait

    fun ifItsTv(body: () -> Unit) { if (isTv) body() } // I'm lazy :D
}

