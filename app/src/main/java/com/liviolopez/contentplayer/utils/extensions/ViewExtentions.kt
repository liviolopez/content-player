package com.liviolopez.contentplayer.utils.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.setGone() { if(visibility != View.GONE) visibility = View.GONE }
fun View.setVisible() { if(visibility != View.VISIBLE) visibility = View.VISIBLE }

inline fun <T : View> T.visibleIf(isTrue: (T) -> Boolean) {
    if (isTrue(this))
        this.setVisible()
    else
        this.setGone()
}

fun View.showSnackBar(
    msg: String,
    paddingBottom: Int? = 0,
    duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, msg, duration).apply {
        view.translationY = resources.displayMetrics.density * (paddingBottom!! * -1)
        show()
    }
}