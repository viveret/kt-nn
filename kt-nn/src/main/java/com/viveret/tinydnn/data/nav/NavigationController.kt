package com.viveret.tinydnn.data.nav

import java.io.File

interface NavigationController {
    val location: String
    fun back()
    fun forward()
    fun replace(location: File)
    fun assign(location: File)
    fun reload()

    fun addListener(listener: NavigationListener)
    fun removeListener(listener: NavigationListener)
}