package com.viveret.tinydnn.util

import android.content.Context
import com.viveret.tinydnn.util.async.AppLifecycleListener

class AppLifecycleContext(val context: Context): AppLifecycleListener {
    override fun onCreate() = this.listeners.forEach{ x -> x.onCreate() }

    override fun onStart() = this.listeners.forEach{ x -> x.onStart() }

    override fun onResume() = this.listeners.forEach{ x -> x.onResume() }

    override fun onPause() = this.listeners.forEach{ x -> x.onPause() }

    override fun onStop() = this.listeners.forEach{ x -> x.onStop() }

    override fun onDestroy() = this.listeners.forEach{ x -> x.onDestroy() }

    fun addListener(listener: AppLifecycleListener) {
        this.listeners.add(listener)
    }

    private val listeners = ArrayList<AppLifecycleListener>()
}