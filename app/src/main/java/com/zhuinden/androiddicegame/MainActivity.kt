package com.zhuinden.androiddicegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val random = Random()

    private var roll1: Int = 0
        set(value) {
            field = value
            textPlayer1Roll.text = "$value"
        }

    private var roll2: Int = 0
        set(value) {
            field = value
            textPlayer2Roll.text = "$value"
        }

    private var score1: Int = 0
        set(value) {
            field = value
            textPlayer1Score.text = "$value"
        }

    private var score2: Int = 0
        set(value) {
            field = value
            textPlayer2Score.text = "$value"
        }

    private var shouldEvaluate = false
        set(value) {
            field = value
            if (value) {
                handler.postDelayed(1000L) {
                    doEvaluate()
                }
            }
        }

    private var didPlayerWin = false
        set(value) {
            field = value
            if (!value) {
                textPlayerWon.hide()
                groupRoll.show()
                buttonRoll.text = getString(R.string.game_button_action_roll)
            } else {
                groupRoll.hide()
                textPlayerWon.show()
                textPlayerWon.text = when {
                    score1 == 9 -> getString(R.string.game_state_player_1_won)
                    else -> getString(R.string.game_state_player_2_won)
                }
                buttonRoll.text = getString(R.string.game_button_action_reset)
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.run {
            putInt("roll1", roll1)
            putInt("roll2", roll2)
            putInt("score1", score1)
            putInt("score2", score2)
            putBoolean("shouldEvaluate", shouldEvaluate)
            putBoolean("didPlayerWin", didPlayerWin)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.run {
            roll1 = getInt("roll1", 0);
            roll2 = getInt("roll2", 0);
            score1 = getInt("score1", 0)
            score2 = getInt("score2", 0)
            shouldEvaluate = getBoolean("shouldEvaluate")
            didPlayerWin = getBoolean("didPlayerWin")
        }

        buttonRoll.onClick {
            if (score1 == 9 || score2 == 9) {
                score1 = 0
                score2 = 0

                didPlayerWin = false
                return@onClick
            }

            val roll = random.nextInt(6) + 1

            if (roll1 == 0) {
                roll1 = roll
            } else {
                roll2 = roll

                shouldEvaluate = true
            }
        }
    }

    private fun doEvaluate() {
        shouldEvaluate = false
        if (roll1 > roll2) {
            score1++
        } else if (roll1 < roll2) {
            score2++
        }

        roll1 = 0
        roll2 = 0

        if (score1 == 9 || score2 == 9) {
            didPlayerWin = true
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return !shouldEvaluate && super.dispatchTouchEvent(ev)
    }
}