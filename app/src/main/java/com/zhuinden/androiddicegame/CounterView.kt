package com.zhuinden.androiddicegame

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.zhuinden.androiddicegame.utils.onClick
import kotlinx.android.synthetic.main.counter_view.view.*

class CounterView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var listener: Listener? = null

    interface Listener {
        fun onButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // i am the controller!!
        buttonRoll.onClick {
            listener?.onButtonClicked()
        }
    }
}