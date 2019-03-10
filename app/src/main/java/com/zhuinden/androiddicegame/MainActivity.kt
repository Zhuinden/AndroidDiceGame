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
    private var roll2: Int = 0

    private var score1: Int = 0
    private var score2: Int = 0

    private var shouldEvaluate = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.run {
            putInt("roll1", roll1)
            putInt("roll2", roll2)
            putInt("score1", score1)
            putInt("score2", score2)
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
        }

        buttonRoll.onClick {
            if(score1 == 9 || score2 == 9) {
                score1 = 0
                textPlayer1Score.text = "${score1}"
                score2 = 0
                textPlayer2Score.text = "${score2}"
                textPlayerWon.hide()
                groupRoll.show()
                buttonRoll.text = "Roll"
                return@onClick
            }

            val roll = random.nextInt(6) + 1

            if (roll1 == 0) {
                roll1 = roll
                textPlayer1Roll.text = "${roll1}"
            } else {
                roll2 = roll
                textPlayer2Roll.text = "${roll2}"

                shouldEvaluate = true
                handler.postDelayed(1000L) {
                    shouldEvaluate = false
                    if (roll1 > roll2) {
                        score1++
                        textPlayer1Score.text = "${score1}"
                    } else if (roll1 < roll2) {
                        score2++
                        textPlayer2Score.text = "${score2}"
                    }

                    roll1 = 0
                    textPlayer1Roll.text = "${roll1}"
                    roll2 = 0
                    textPlayer2Roll.text = "${roll2}"

                    if (score1 == 9 || score2 == 9) {
                        groupRoll.hide()
                        textPlayerWon.show()
                        textPlayerWon.text = when {
                            score1 == 9 -> "Player 1 won!"
                            else -> "Player 2 won!"
                        }

                        buttonRoll.text = "Reset"
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return !shouldEvaluate && super.dispatchTouchEvent(ev)
    }
}
