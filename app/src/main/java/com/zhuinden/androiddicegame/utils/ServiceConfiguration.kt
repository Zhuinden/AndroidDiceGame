package com.zhuinden.androiddicegame.utils

import com.zhuinden.simplestack.ScopedServices

class ServiceConfiguration : ScopedServices {
    override fun bindServices(serviceBinder: ScopedServices.ServiceBinder) {
        val key = serviceBinder.getKey<Any>()
        if (key is HasServices) {
            key.bindServices(serviceBinder)
        }
    }
}