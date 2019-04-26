package com.zhuinden.androiddicegame

import com.zhuinden.androiddicegame.utils.ViewKey
import com.zhuinden.androiddicegame.utils.addService
import com.zhuinden.simplestack.ScopedServices
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CounterKey(private val placeholder: String = "") : ViewKey() {
    override fun layout(): Int = R.layout.counter_view

    // is this really where I belong?
    override fun bindServices(serviceBinder: ScopedServices.ServiceBinder) {
        // clearly there must be a better way to resolve this object graph
        val diceGame = DiceGame(
            DiceGameDelayProvider(),
            DiceGameRollProvider()
        )

        serviceBinder.addService(diceGame)

        @Suppress("RemoveExplicitTypeArguments")
        serviceBinder.addService<CounterView.Listener>(diceGame.inputs)
    }
}