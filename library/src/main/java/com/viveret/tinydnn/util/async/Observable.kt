package com.viveret.tinydnn.util.async

interface Observable {
    fun addListener(listener: OnItemSelectedListener)
    fun removeListener(listener: OnItemSelectedListener)
    val listeners: ArrayList<OnItemSelectedListener>
}