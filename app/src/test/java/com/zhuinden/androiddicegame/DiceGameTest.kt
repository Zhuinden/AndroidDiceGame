package com.zhuinden.androiddicegame

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class DiceGameTest {
    private lateinit var compositeDisposable: CompositeDisposable

    private var roll1: Int = 0
    private var roll2: Int = 0
    private var score1: Int = 0
    private var score2: Int = 0
    private var winner: DiceGame.Winner = DiceGame.Winner.NONE

    private lateinit var delayProvider: DelayProvider
    private lateinit var diceGame: DiceGame

    private class DelayProvider : DiceGame.DelayProvider {
        private var action: (() -> Unit)? = null

        fun triggerAction() {
            val action = action
            this.action = null
            if (action != null) {
                action()
            }
        }

        override fun executeDelayed(delay: Long, action: () -> Unit) {
            this.action = action
        }
    }

    private class DiceRollProvider(
        vararg rolls: Int
    ) : DiceGame.DiceRollProvider {
        private val values: List<Int>

        init {
            val listBuilder = mutableListOf<Int>()
            for (roll in rolls) {
                listBuilder.add(roll)
            }
            values = listBuilder.toList()

            if (values.isEmpty()) {
                throw IllegalArgumentException("Values list cannot be empty!")
            }
        }

        private var index: Int = 0

        override fun rollD6(): Int {
            if (index >= values.size) {
                index = 0
            }
            return values.get(index++)
        }
    }

    @Before
    fun before() {
        compositeDisposable = CompositeDisposable()
        delayProvider = DelayProvider()
    }

    @After
    fun after() {
        compositeDisposable.clear()
        roll1 = 0
        roll2 = 0
        score1 = 0
        score2 = 0
        winner = DiceGame.Winner.NONE
    }

    private fun bindObservers() {
        compositeDisposable += diceGame.roll1().subscribeBy { roll1 = it }
        compositeDisposable += diceGame.roll2().subscribeBy { roll2 = it }
        compositeDisposable += diceGame.score1().subscribeBy { score1 = it }
        compositeDisposable += diceGame.score2().subscribeBy { score2 = it }
        compositeDisposable += diceGame.winner().subscribeBy { winner = it }
    }

    private fun buildDiceGame(
        delayProvider: DiceGame.DelayProvider,
        diceRollProvider: DiceGame.DiceRollProvider
    ): DiceGame =
        DiceGame(delayProvider, diceRollProvider).also {
            diceGame = it
            bindObservers()
        }

    private fun DiceGame.triggerEvent() = inputs.onButtonClicked()

    @Test
    fun roll1IsSet() {
        val game = buildDiceGame(delayProvider, DiceRollProvider(1))
        game.triggerEvent()
        assertThat(roll1).isEqualTo(1)
    }

    @Test
    fun roll2IsSet() {
        val game = buildDiceGame(delayProvider, DiceRollProvider(1, 2))
        game.triggerEvent()
        game.triggerEvent()
        assertThat(roll2).isEqualTo(2)
    }

    @Test
    fun nothingHappensDuringDelay() {
        val game = buildDiceGame(delayProvider, DiceRollProvider(1, 2))
        game.triggerEvent()
        game.triggerEvent()
        // delay should happen here
        game.triggerEvent()
        game.triggerEvent()
        game.triggerEvent()

        assertThat(roll1).isEqualTo(1)
        assertThat(roll2).isEqualTo(2)
        assertThat(score1).isEqualTo(0)
        assertThat(score2).isEqualTo(0)
        assertThat(winner).isSameAs(DiceGame.Winner.NONE)
    }

    @Test
    fun score1IsEvaluatedAfterDelay() {
        val game = buildDiceGame(delayProvider, DiceRollProvider(2, 1))
        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction()

        assertThat(roll1).isEqualTo(0)
        assertThat(roll2).isEqualTo(0)
        assertThat(score1).isEqualTo(1)
        assertThat(score2).isEqualTo(0)
        assertThat(winner).isSameAs(DiceGame.Winner.NONE)
    }

    @Test
    fun score2IsEvaluatedAfterDelay() {
        val game = buildDiceGame(delayProvider, DiceRollProvider(1, 2))
        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction()

        assertThat(roll1).isEqualTo(0)
        assertThat(roll2).isEqualTo(0)
        assertThat(score1).isEqualTo(0)
        assertThat(score2).isEqualTo(1)
        assertThat(winner).isSameAs(DiceGame.Winner.NONE)
    }

    @Test
    fun drawIncrementsNoOnesGame() {
        val game = buildDiceGame(delayProvider, DiceRollProvider(5, 5))
        game.triggerEvent()
        game.triggerEvent()

        assertThat(roll1).isEqualTo(5)
        assertThat(roll2).isEqualTo(5)

        delayProvider.triggerAction()

        assertThat(roll1).isEqualTo(0)
        assertThat(roll2).isEqualTo(0)
        assertThat(score1).isEqualTo(0)
        assertThat(score2).isEqualTo(0)
        assertThat(winner).isSameAs(DiceGame.Winner.NONE)
    }

    @Test
    fun reaching9TriggersWinPlayer1() {
        val game = buildDiceGame(
            delayProvider, DiceRollProvider(
                5, 1, // 1
                5, 1, // 2
                5, 1, // 3
                5, 1, // 4
                5, 1, // 5
                5, 1, // 6 
                5, 1, // 7
                5, 1, // 8
                5, 1  // 9
            )
        )
        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 1

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 2

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 3

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 4

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 5

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 6

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 7

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 8

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 9

        assertThat(roll1).isEqualTo(0)
        assertThat(roll2).isEqualTo(0)
        assertThat(score1).isEqualTo(9)
        assertThat(score2).isEqualTo(0)
        assertThat(winner).isSameAs(DiceGame.Winner.PLAYER_1)
    }

    @Test
    fun reaching9TriggersWinPlayer2() {
        val game = buildDiceGame(
            delayProvider, DiceRollProvider(
                1, 5, // 1
                1, 5, // 2
                1, 5, // 3
                1, 5, // 4
                1, 5, // 5
                1, 5, // 6 
                1, 5, // 7
                1, 5, // 8
                1, 5  // 9
            )
        )
        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 1

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 2

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 3

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 4

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 5

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 6

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 7

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 8

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 9

        assertThat(roll1).isEqualTo(0)
        assertThat(roll2).isEqualTo(0)
        assertThat(score1).isEqualTo(0)
        assertThat(score2).isEqualTo(9)
        assertThat(winner).isSameAs(DiceGame.Winner.PLAYER_2)
    }

    @Test
    fun pressingButtonAfterWinResetsGame() {
        val game = buildDiceGame(
            delayProvider, DiceRollProvider(
                1, 5, // 1
                1, 5, // 2
                1, 5, // 3
                1, 5, // 4
                1, 5, // 5
                1, 5, // 6
                1, 5, // 7
                1, 5, // 8
                1, 5  // 9
            )
        )
        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 1

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 2

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 3

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 4

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 5

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 6

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 7

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 8

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 9

        // Victory happened here

        game.triggerEvent()

        assertThat(roll1).isEqualTo(0)
        assertThat(roll2).isEqualTo(0)
        assertThat(score1).isEqualTo(0)
        assertThat(score2).isEqualTo(0)
        assertThat(winner).isSameAs(DiceGame.Winner.NONE)
    }

    @Test
    fun gameCanBePlayedAfterReset() {
        val game = buildDiceGame(
            delayProvider, DiceRollProvider(
                1, 5, // 1
                1, 5, // 2
                1, 5, // 3
                1, 5, // 4
                1, 5, // 5
                1, 5, // 6
                1, 5, // 7
                1, 5, // 8
                1, 5, // 9
                5, 2 // next game
            )
        )
        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 1

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 2

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 3

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 4

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 5

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 6

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 7

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 8

        game.triggerEvent()
        game.triggerEvent()
        delayProvider.triggerAction() // 9

        // Victory happened here

        game.triggerEvent()

        // Reset happened here

        game.triggerEvent()
        game.triggerEvent()

        assertThat(roll1).isEqualTo(5)
        assertThat(roll2).isEqualTo(2)
        assertThat(score1).isEqualTo(0)
        assertThat(score2).isEqualTo(0)

        delayProvider.triggerAction()
        assertThat(roll1).isEqualTo(0)
        assertThat(roll2).isEqualTo(0)
        assertThat(score1).isEqualTo(1)
        assertThat(score2).isEqualTo(0)
    }

    // TODO: state persistence tests through Bundleable.
}
