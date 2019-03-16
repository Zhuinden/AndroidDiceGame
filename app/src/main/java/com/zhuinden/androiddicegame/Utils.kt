package com.zhuinden.androiddicegame

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

inline fun View.onClick(crossinline handler: (View) -> Unit) {
    setOnClickListener { view ->
        handler(view)
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun View.showIf(predicate: (View) -> Boolean) {
    if (predicate(this)) {
        show()
    } else {
        hide()
    }
}

//// from Anko Dimensions.kt
//returns dip(dp) dimension value in pixels
fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dp(value: Float): Int = (value * resources.displayMetrics.density).toInt()

//return sp dimension value in pixels
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

//converts px value into dip or sp
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity
////

fun View.dp(value: Int): Int = context.dp(value)
fun View.dp(value: Float): Int = context.dp(value)

val Int.f: Float get() = this.toFloat()

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Handler.postDelayed(delay: Long, action: () -> Unit) {
    postDelayed(action, delay)
}