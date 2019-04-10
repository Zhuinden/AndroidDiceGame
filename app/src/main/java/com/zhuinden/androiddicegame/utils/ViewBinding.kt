package com.zhuinden.androiddicegame.utils

import android.view.View

abstract class ViewBinding<V : View>(
    protected val view: V
) {
    init {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                if (v === view) {
                    view.onAttach()
                }
            }

            override fun onViewDetachedFromWindow(v: View?) {
                if (v === view) {
                    view.onDetach()
                }
            }
        })
    }

    protected abstract fun V.onAttach()

    protected abstract fun V.onDetach()
}