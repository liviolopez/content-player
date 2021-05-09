package com.liviolopez.contentplayer.ui._components

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.liviolopez.contentplayer.R
import com.liviolopez.contentplayer.databinding.ViewOverlayStandbyBinding
import com.liviolopez.contentplayer.utils.extensions.setGone
import com.liviolopez.contentplayer.utils.extensions.setVisible

/**
 * custom view with viewBinding delegation to overlay while waiting for responses from data sources
 */

class OverlayStandbyView(context: Context) : ConstraintLayout(context) {
    init{
        inflate(context, R.layout.view_overlay_standby, this)
    }
}

val ViewOverlayStandbyBinding.initState
    get() = run {
        standbyViewContainer.setVisible()

        progressBarContainer.setGone()
        emptyList.setGone()

        errorContainer.setGone()
        btRetry.setGone()
        btRetry.setOnClickListener(null)

        ivErrorImage.setImageDrawable(
            ContextCompat.getDrawable(
                ivErrorImage.context,
                R.drawable.ic_broken_link
            )
        )
    }

val ViewOverlayStandbyBinding.success
    get() = standbyViewContainer.setGone()

val ViewOverlayStandbyBinding.loading
    get() = run { initState
        progressBarContainer.setVisible()
    }

var ViewOverlayStandbyBinding.showError: String?
    get() =  run {
        this.showError = this.root.resources.getString(R.string.error_msg); null
    }
    set(msg) {
        initState
        errorContainer.setVisible()
        tvError.text = msg ?: ""
    }

val ViewOverlayStandbyBinding.showEmptyMsg
    get() = run { initState
        emptyList.setVisible()
    }

fun ViewOverlayStandbyBinding.showEmptyMsg(view: View, msg: String) {
    tvEmptyList.text = msg
    this showEmptyMsg view
}

infix fun ViewOverlayStandbyBinding.showEmptyMsg(view: View) {
    showEmptyMsg
    view.setGone()
}