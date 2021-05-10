package com.liviolopez.contentplayer.utils.extensions

import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.liviolopez.contentplayer.R

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

fun <T> TextInputLayout.setOptions(
    list: List<T>,
    show: (T) -> String,
    currentValIf: (T?) -> Boolean = { false },
    onClick: (T?) -> Unit,
    topValue: String? = null
) {
    val context = this.context
    val _topValue = topValue?.let { listOf(it) } ?: emptyList()

    val adapter = ArrayAdapter(context, R.layout.text_item, _topValue + list.map { show(it) })
    (this.editText as? AutoCompleteTextView)?.apply {
        setAdapter(adapter)
        list.firstOrNull { currentValIf(it) }?.let {
            setText(show(it))
        }
        setOnItemClickListener { _, _, position, _ ->
            val item = when {
                _topValue.isEmpty() -> { list[position] }
                position > 0 -> { list[position - 1] }
                else -> null
            }

            onClick(item)
        }
    }
}