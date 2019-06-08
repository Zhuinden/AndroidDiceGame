package com.zhuinden.androiddicegame.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhuinden.androiddicegame.CounterKey
import com.zhuinden.androiddicegame.R
import com.zhuinden.androiddicegame.utils.ServiceConfiguration
import com.zhuinden.androiddicegame.utils.ViewStateChanger
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.navigator.Navigator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigator.configure()
            .setScopedServices(ServiceConfiguration())
            .setStateChanger(ViewStateChanger(this, containerRoot))
            .install(this, containerRoot, History.of(CounterKey()))
    }

    override fun onBackPressed() {
        if (!Navigator.onBackPressed(this)) {
            super.onBackPressed()
        }
    }
}