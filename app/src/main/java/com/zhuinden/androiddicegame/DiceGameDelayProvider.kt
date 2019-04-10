package com.zhuinden.androiddicegame

import android.os.Handler
import android.os.Looper

class DiceGameDelayProvider : DiceGame.DelayProvider {
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun executeDelayed(delay: Long, action: () -> Unit) {
        handler.postDelayed(action, delay)
    }
}