package com.zhuinden.androiddicegame

import android.os.Handler
import android.os.Looper
import com.zhuinden.androiddicegame.utils.addService
import com.zhuinden.simplestack.ScopedServices
import java.util.*

class ServiceConfiguration : ScopedServices {
    override fun bindServices(serviceBinder: ScopedServices.ServiceBinder) {
        when (serviceBinder.scopeTag) {
            CounterKey.SCOPE_TAG -> {
                val diceGame = DiceGame(
                    // TODO: this should be its own class and it should come from app component (constr param)
                    object : DiceGame.DelayProvider {
                        private val handler: Handler = Handler(Looper.getMainLooper())

                        override fun executeDelayed(delay: Long, action: () -> Unit) {
                            handler.postDelayed(action, delay)
                        }
                    },
                    // TODO: this should be its own class and it should come from app component (constr param)
                    object : DiceGame.DiceRollProvider {
                        private val random: Random = Random()

                        override fun rollD6(): Int = random.nextInt(6) + 1
                    }
                )

                serviceBinder.addService(diceGame)

                @Suppress("RemoveExplicitTypeArguments")
                serviceBinder.addService<CounterView.Listener>(diceGame.inputs)
            }
        }
    }
}