package com.viveret.tinydnn.data.nav

import android.util.Log
import com.viveret.tinydnn.util.async.OnItemSelectedListener
import com.viveret.tinydnn.util.async.OnSelectedResult
import com.viveret.tinydnn.util.nav.NavigationItem
import java.io.File
import java.util.*
import kotlin.collections.HashMap

open class FileNavigator : NavigationController, NavigationListener, OnItemSelectedListener {
    private val virtualFileSystemPaths = HashMap<String, (String) -> Array<NavigationItem>>()
    private val redirectFilePaths = HashMap<String, String>()

    fun addVirtualPath(destPath: String, resolver: (String) -> Array<NavigationItem>) {
        virtualFileSystemPaths[destPath] = resolver
    }

    fun addSymLink(sourcePath: String, destPath: String) {
        virtualFileSystemPaths[destPath] = { navItemsAt(File(sourcePath)) }
    }

    fun addRedirect(sourcePath: String, destPath: String) {
        redirectFilePaths[destPath] = sourcePath
    }

    init {
        // addRedirect("/storage/emulated", "/storage/emulated/0")
    }

    override fun onSelected(item: Any): OnSelectedResult {
        return if (item is NavigationItem) {
            OnSelectedResult(true) {
                this.assign(item.file)
            }
        } else {
            OnSelectedResult(false)
        }
    }

    override fun onHistoryChange(history: Array<String>) {

    }

    override fun onItemsChange(items: Array<NavigationItem>) {
        for (listener in this.listeners) {
            listener.onItemsChange(items)
        }
    }

    override fun onLocationChange(location: File) {
        val path = location.canonicalPath
        when {
            redirectFilePaths.containsKey(path) -> onLocationChange(File(redirectFilePaths[path]!!))
            virtualFileSystemPaths.containsKey(path) -> onChange(location, virtualFileSystemPaths[path]!!(path))
            else -> {
                //val currentFolder = File(if (location.isNotEmpty()) location else "/")
                Log.e("com.viveret.pocketn2", "Traversing $path")
                val currentEntries = navItemsAt(location)
                onChange(location, currentEntries)
            }
        }
    }

    protected fun navItemsAt(currentFolder: File): Array<NavigationItem> =
            if (currentFolder.exists()) {
                if (currentFolder.isDirectory) {
                    if (currentFolder.canRead()) {
                        try {
                            val items = currentFolder.list() ?: emptyArray()
                            if (items.isEmpty()) emptyArray() else items.map { x -> NavigationItem(location, x) }.toTypedArray()
                        } catch (e: Exception) {
                            throw Exception("Could not list directory $location", e)
                        }
                    } else {
                        throw Exception("Location \"$location\" is not readable")
                    }
                } else {
                    throw Exception("Location \"$location\" not a directory")
                }
            } else {
                throw Exception("Location \"$location\" does not exist")
            }

    private fun onChange(location: File, entries: Array<NavigationItem>) {
        for (listener in this.listeners) {
            listener.onLocationChange(location)
        }
        this.onItemsChange(entries)
    }

    private val history = LinkedList<String>()
    private val listeners = ArrayList<NavigationListener>()
    private var historyIndex = 0

    override val location: String
        get() = history[historyIndex]

    override fun back() {
        historyIndex -= 1
        this.onLocationChange(File(this.location))
    }

    override fun forward() {
        historyIndex += 1
        this.onLocationChange(File(this.location))
    }

    override fun replace(location: File) {
        if (history.isNotEmpty()) {
            this.history.removeLast()
        }
        this.history.add(location.canonicalPath)
        this.onLocationChange(File(this.location))
    }

    override fun assign(location: File) {
        this.history.add(location.canonicalPath) // if (location.endsWith('/')) location.substring(0, location.length - 1) else location
        this.historyIndex++
        this.onHistoryChange(this.history.toTypedArray())
        this.onLocationChange(File(this.location))
    }

    override fun reload() = this.onLocationChange(File(this.location))

    override fun addListener(listener: NavigationListener) {
        this.listeners.add(listener)
    }

    override fun removeListener(listener: NavigationListener) {
        this.listeners.remove(listener)
    }
}