package com.zhuinden.androiddicegame

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.zhuinden.androiddicegame.utils.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.counter_view.view.*

class CounterView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val listener by lazy { lookup<Listener>() }

    private val model by lazy { lookup<DiceGame>() } // he is the model, but can I know him by direct type?

    interface Listener {
        fun onButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // i am the controller?
        buttonRoll.onClick {
            listener.onButtonClicked()
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // i am the view
        compositeDisposable += model.roll1().subscribeBy { value ->
            textPlayer1Roll.text = "$value"
        }
        compositeDisposable += model.roll2().subscribeBy { value ->
            textPlayer2Roll.text = "$value"
        }
        compositeDisposable += model.score1().subscribeBy { value ->
            textPlayer1Score.text = "$value"
        }
        compositeDisposable += model.score2().subscribeBy { value ->
            textPlayer2Score.text = "$value"
        }
        compositeDisposable += model.winner().subscribeBy { value ->
            when (value) {
                DiceGame.Winner.NONE -> {
                    textPlayerWon.hide()
                    groupRoll.show()
                    buttonRoll.text = getString(R.string.game_button_action_roll)
                }
                DiceGame.Winner.PLAYER_1, DiceGame.Winner.PLAYER_2 -> {
                    groupRoll.hide()
                    textPlayerWon.show()
                    textPlayerWon.text = when {
                        value == DiceGame.Winner.PLAYER_1 -> getString(R.string.game_state_player_1_won)
                        else -> getString(R.string.game_state_player_2_won)
                    }
                    buttonRoll.text = getString(R.string.game_button_action_reset)
                }
            }.safe()
        }
    }

    override fun onDetachedFromWindow() {
        compositeDisposable.clear()
        super.onDetachedFromWindow()
    }
}