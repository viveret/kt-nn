package com.viveret.tinydnn.data.nav

import java.io.File

class LocalFileInfo(val f: File): FileInfo {
    constructor(path: String): this(File(path))

    override val canonicalPath: String = f.canonicalPath
    override val extension: String = f.extension
    override val name: String = f.name
    override val nameWithoutExtension: String = f.nameWithoutExtension
    override val lastModified: Long = f.lastModified()
    override val isDirectory: Boolean = f.isDirectory
}