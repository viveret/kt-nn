package com.viveret.tinydnn.data.nav

import com.viveret.tinydnn.util.nav.NavigationItem

interface NavigationListener {
    fun onHistoryChange(history: Array<String>)
    fun onItemsChange(items: Array<NavigationItem>)
    fun onLocationChange(location: String)
}