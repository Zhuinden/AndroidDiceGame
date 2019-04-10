package com.zhuinden.androiddicegame

import java.util.*

class DiceGameRollProvider : DiceGame.DiceRollProvider {
    private val random: Random = Random()

    override fun rollD6(): Int = random.nextInt(6) + 1
}