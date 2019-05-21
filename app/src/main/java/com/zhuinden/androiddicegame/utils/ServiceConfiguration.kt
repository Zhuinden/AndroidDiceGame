package com.zhuinden.androiddicegame.utils

import com.zhuinden.simplestack.ScopedServices
import com.zhuinden.simplestack.ServiceBinder

class ServiceConfiguration : ScopedServices {
    override fun bindServices(serviceBinder: ServiceBinder) {
        val key = serviceBinder.getKey<Any>()
        if (key is HasServices) {
            key.bindServices(serviceBinder)
        }
    }
}