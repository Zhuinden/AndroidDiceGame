package com.zhuinden.androiddicegame

import com.jakewharton.rxrelay2.BehaviorRelay
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import io.reactivex.Observable

// i am the model
class DiceGame(
    private val delayProvider: DelayProvider,
    private val diceRollProvider: DiceRollProvider
) : Bundleable {
    val inputs = object : CounterView.Listener {
        override fun onButtonClicked() {
            executeGameEvent()
        }
    }

    interface DelayProvider {
        fun executeDelayed(delay: Long, action: () -> Unit)
    }

    interface DiceRollProvider {
        fun rollD6(): Int
    }

    private var roll1: Int = 0
        set(value) {
            field = value
            _roll1.accept(value)
        }

    private var roll2: Int = 0
        set(value) {
            field = value
            _roll2.accept(value)
        }

    private var score1: Int = 0
        set(value) {
            field = value
            _score1.accept(value)
        }

    private var score2: Int = 0
        set(value) {
            field = value
            _score2.accept(value)
        }

    private var shouldEvaluate = false
        set(value) {
            field = value
            if (value) {
                delayProvider.executeDelayed(1000L) {
                    doEvaluate()
                }
            }
        }

    @Suppress("IntroduceWhenSubject")
    private var didPlayerWin = false
        set(value) {
            field = value
            _winner.accept(
                when {
                    !value -> Winner.NONE
                    else -> when {
                        score1 == 9 -> Winner.PLAYER_1
                        else -> Winner.PLAYER_2
                    }
                }
            )
        }

    private var _roll1: BehaviorRelay<Int> = BehaviorRelay.createDefault(0)
    fun roll1(): Observable<Int> = _roll1

    private var _roll2: BehaviorRelay<Int> = BehaviorRelay.createDefault(0)
    fun roll2(): Observable<Int> = _roll2

    private var _score1: BehaviorRelay<Int> = BehaviorRelay.createDefault(0)
    fun score1(): Observable<Int> = _score1

    private var _score2: BehaviorRelay<Int> = BehaviorRelay.createDefault(0)
    fun score2(): Observable<Int> = _score2

    private var _winner: BehaviorRelay<Winner> = BehaviorRelay.createDefault(Winner.NONE)
    fun winner(): Observable<Winner> = _winner

    enum class Winner {
        NONE,
        PLAYER_1,
        PLAYER_2
    }


    private fun executeGameEvent() {
        if (shouldEvaluate) {
            return
        }

        if (score1 == 9 || score2 == 9) {
            score1 = 0
            score2 = 0

            didPlayerWin = false
            return
        }

        val roll = diceRollProvider.rollD6()

        if (roll1 == 0) {
            roll1 = roll
        } else {
            roll2 = roll

            shouldEvaluate = true
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

    override fun toBundle(): StateBundle = StateBundle().apply {
        putInt("roll1", roll1)
        putInt("roll2", roll2)
        putInt("score1", score1)
        putInt("score2", score2)
        putBoolean("shouldEvaluate", shouldEvaluate)
        putBoolean("didPlayerWin", didPlayerWin)
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            roll1 = getInt("roll1", 0);
            roll2 = getInt("roll2", 0);
            score1 = getInt("score1", 0)
            score2 = getInt("score2", 0)
            shouldEvaluate = getBoolean("shouldEvaluate")
            didPlayerWin = getBoolean("didPlayerWin")
        }
    }
}