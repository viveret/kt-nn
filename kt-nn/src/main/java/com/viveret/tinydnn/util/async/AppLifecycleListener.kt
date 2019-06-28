package com.viveret.tinydnn.util.async

interface AppLifecycleListener {
    fun onCreate()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}