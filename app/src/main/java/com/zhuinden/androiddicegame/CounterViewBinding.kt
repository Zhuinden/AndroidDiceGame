package com.zhuinden.androiddicegame

import com.zhuinden.androiddicegame.utils.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.counter_view.view.*

// i am the view!! but was it worth it?
class CounterViewBinding(
    private val model: DiceGame,
    counterView: CounterView
) : ViewBinding<CounterView>(counterView) {
    private val compositeDisposable = CompositeDisposable()

    override fun CounterView.onAttach() {
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

    override fun CounterView.onDetach() {
        compositeDisposable.clear()
    }
}