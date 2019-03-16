package com.zhuinden.androiddicegame

import com.zhuinden.androiddicegame.utils.ViewKey
import com.zhuinden.simplestack.ScopeKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CounterKey(private val placeholder: String = "") : ViewKey(), ScopeKey {
    override fun layout(): Int = R.layout.counter_view

    override fun getScopeTag(): String = SCOPE_TAG

    companion object {
        const val SCOPE_TAG = "CounterKey"
    }
}