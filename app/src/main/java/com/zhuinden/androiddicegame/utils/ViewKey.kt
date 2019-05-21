package com.zhuinden.androiddicegame.utils

import android.os.Parcelable
import com.zhuinden.simplestack.navigator.DefaultViewKey
import com.zhuinden.simplestack.navigator.ViewChangeHandler
import com.zhuinden.simplestack.navigator.changehandlers.FadeViewChangeHandler

abstract class ViewKey : DefaultViewKey, HasServices, Parcelable {
    override fun getScopeTag(): String = this.javaClass.name

    override fun viewChangeHandler(): ViewChangeHandler = FadeViewChangeHandler()
}