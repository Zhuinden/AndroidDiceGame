package com.zhuinden.androiddicegame.utils

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.jakewharton.rxrelay2.BehaviorRelay
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.BackstackManager
import com.zhuinden.simplestack.ScopedServices
import com.zhuinden.simplestack.navigator.Navigator
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

fun <T> T.safe() = Unit

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

fun View.getString(@StringRes stringRes: Int) = context.getString(stringRes)

fun View.getString(@StringRes stringRes: Int, vararg formatArgs: Any) = context.getString(stringRes, *formatArgs)

inline fun View.showIf(predicate: (View) -> Boolean) {
    if (predicate(this)) {
        show()
    } else {
        hide()
    }
}

// services
inline fun <reified T> Context.lookup(): T = Navigator.lookupService(this, T::class.java.name)

inline fun <reified T> ScopedServices.ServiceBinder.addService(service: T) {
    add(T::class.java.name, service as Any)
}

inline fun <reified T> View.lookup(): T = context.lookup()

inline fun <reified T> BackstackManager.lookup(): T = lookupService(T::class.java.name)

// navigation (though I'm not using it atm)
val Context.backstack: Backstack
    get() = Navigator.getBackstack(this)

val View.backstack
    get() = context.backstack

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

////
fun <T> exposed(initialValue: T, relay: BehaviorRelay<T>) = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        if (newValue != null) {
            relay.accept(newValue)
        }
    }
}