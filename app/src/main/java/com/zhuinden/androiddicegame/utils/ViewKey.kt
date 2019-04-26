package com.zhuinden.androiddicegame.utils

import android.os.Parcelable
import com.zhuinden.simplestack.navigator.StateKey
import com.zhuinden.simplestack.navigator.ViewChangeHandler
import com.zhuinden.simplestack.navigator.changehandlers.FadeViewChangeHandler

abstract class ViewKey : StateKey, HasServices, Parcelable {
    override fun getScopeTag(): String = this.javaClass.name

    override fun viewChangeHandler(): ViewChangeHandler = FadeViewChangeHandler()
}