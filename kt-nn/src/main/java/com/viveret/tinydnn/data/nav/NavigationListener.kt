package com.viveret.tinydnn.data.nav

import com.viveret.tinydnn.util.nav.NavigationItem
import java.io.File

interface NavigationListener {
    fun onHistoryChange(history: Array<String>)
    fun onItemsChange(items: Array<NavigationItem>)
    fun onLocationChange(location: File)
}