package com.zhuinden.androiddicegame

import com.zhuinden.androiddicegame.utils.ViewKey
import com.zhuinden.androiddicegame.utils.add
import com.zhuinden.androiddicegame.utils.rebind
import com.zhuinden.simplestack.ScopedServices
import com.zhuinden.simplestack.ServiceBinder
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CounterKey(private val placeholder: String = "") : ViewKey() {
    override fun layout(): Int = R.layout.counter_view

    // is this really where I belong?
    override fun bindServices(serviceBinder: ServiceBinder) {
        // clearly there must be a better way to resolve this object graph
        val diceGame = DiceGame(
            DiceGameDelayProvider(),
            DiceGameRollProvider()
        )

        serviceBinder.add(diceGame)

        @Suppress("RemoveExplicitTypeArguments")
        serviceBinder.add<CounterView.Listener>(diceGame.inputs)
    }
}