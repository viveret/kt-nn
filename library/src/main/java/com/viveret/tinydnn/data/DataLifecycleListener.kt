package com.viveret.tinydnn.data

interface DataLifecycleListener {
    fun onCached(item: Any)
    fun onFreed(item: Any)
}