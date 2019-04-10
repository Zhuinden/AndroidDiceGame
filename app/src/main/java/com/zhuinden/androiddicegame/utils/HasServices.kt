package com.zhuinden.androiddicegame.utils

import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestack.ScopedServices

interface HasServices : ScopeKey {
    fun bindServices(serviceBinder: ScopedServices.ServiceBinder)
}