package com.viveret.tinydnn.util.nav

import com.viveret.tinydnn.R
import com.viveret.tinydnn.data.nav.FileInfo
import com.viveret.tinydnn.data.nav.LocalFileInfo
import java.io.File

class NavigationItem {
    var file: FileInfo
    val iconResId: Int
    var lastModified: Long
    val name: String

    constructor(file: FileInfo) {
        this.file = file
        this.name = file.name
        this.lastModified = file.lastModified
        iconResId = if (file.isDirectory) {
            R.drawable.ic_folder_black_24dp
        } else {
            when (file.extension.toLowerCase()) {
                "png", "jpg", "jpeg", "bmp", "gif", "pdf" -> R.drawable.ic_image_black_24dp
                "json" -> R.drawable.ic_android_black_24dp
                else -> R.drawable.ic_file_black_24dp
            }
        }
    }

    constructor(path: String) : this(LocalFileInfo(path))

    constructor(directory: String, file: String) : this(LocalFileInfo(File(directory, file)))
}