package com.zhuinden.androiddicegame.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomDashedLineView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val paint: Paint = Paint().apply {
        color = Color.parseColor("#000000")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var current = 0
        val width = width
        while(current < width) {
            canvas.drawLine(current.f, height.f / 2 - dp(1), current + dp(2).f, height.f / 2 + dp(1), paint)
            current += dp(4)
        }
    }
}