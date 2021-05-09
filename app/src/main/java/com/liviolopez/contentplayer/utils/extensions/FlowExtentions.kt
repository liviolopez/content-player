package com.liviolopez.contentplayer.utils.extensions

import android.widget.NumberPicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun NumberPicker.getValueChangeStateFlow(default: Int): StateFlow<Int> {
    val intValue = MutableStateFlow(default)

    setOnValueChangedListener { _, _, newVal ->
        intValue.value = newVal
    }

    return intValue
}