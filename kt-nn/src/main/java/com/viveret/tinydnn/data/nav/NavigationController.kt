package com.viveret.tinydnn.data.nav

interface NavigationController {
    val location: String
    fun back()
    fun forward()
    fun replace(location: String)
    fun assign(location: String)
    fun reload()

    fun addListener(listener: NavigationListener)
    fun removeListener(listener: NavigationListener)
}