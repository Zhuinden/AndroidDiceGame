package com.zhuinden.androiddicegame.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestack.navigator.changehandlers.FadeViewChangeHandler

class BindingEnabledViewStateChanger(
    private val activity: Activity,
    private val container: ViewGroup
) : StateChanger {
    override fun handleStateChange(stateChange: StateChange, completionCallback: StateChanger.Callback) {
        if (stateChange.isTopNewStateEqualToPrevious) {
            completionCallback.stateChangeComplete()
            return
        }

        val previousKey = stateChange.topPreviousState<ViewKey>()
        val previousView: View? = container.getChildAt(0)
        val newKey = stateChange.topNewState<ViewKey>()
        val newView = LayoutInflater.from(stateChange.createContext(activity, newKey))
            .inflate(newKey.layout(), container, false)
        Navigator.persistViewToState(previousView)
        Navigator.restoreViewFromState(newView)

        newKey.bindView(Navigator.getManager(activity), newView)

        if (previousKey == null || previousView == null) {
            container.removeView(previousView)
            container.addView(newView)
            completionCallback.stateChangeComplete()
            return
        }

        val viewChangeHandler = when (stateChange.direction) {
            StateChange.FORWARD -> newKey.viewChangeHandler()
            StateChange.BACKWARD -> previousKey.viewChangeHandler()
            else -> FadeViewChangeHandler()
        }

        viewChangeHandler.performViewChange(container, previousView, newView, stateChange.direction) {
            completionCallback.stateChangeComplete()
        }
    }
}