package com.viveret.tinydnn.data.nav

import com.viveret.tinydnn.util.nav.NavigationItem

class FilteredFileNavigator(val filter: (NavigationItem) -> Boolean): FileNavigator() {
    override fun onItemsChange(items: Array<NavigationItem>) =
            super.onItemsChange(items.filter { x -> filter(x) || x.file.isDirectory }.toTypedArray())
}