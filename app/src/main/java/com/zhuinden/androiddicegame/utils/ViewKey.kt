package com.zhuinden.androiddicegame.utils

import android.os.Parcelable
import android.view.View
import com.zhuinden.simplestack.BackstackManager
import com.zhuinden.simplestack.navigator.StateKey
import com.zhuinden.simplestack.navigator.ViewChangeHandler
import com.zhuinden.simplestack.navigator.changehandlers.FadeViewChangeHandler

abstract class ViewKey : StateKey, HasServices, Parcelable {
    override fun getScopeTag(): String = this.javaClass.name

    override fun viewChangeHandler(): ViewChangeHandler = FadeViewChangeHandler()

    // BackstackManager -> Backstack in simple-stack 2.0
    abstract fun bindView(backstackManager: BackstackManager, view: View)
}