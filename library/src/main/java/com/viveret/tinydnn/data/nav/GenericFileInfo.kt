package com.viveret.tinydnn.data.nav

class GenericFileInfo(override val canonicalPath: String, override val lastModified: Long, override val isDirectory: Boolean) : FileInfo {
    override val name: String
    override val extension: String
    override val nameWithoutExtension: String

    init {
        val nameIndex = canonicalPath.lastIndexOf('/')
        name = if (nameIndex >= 0) canonicalPath.substring(nameIndex) else canonicalPath
        val extensionIndex = name.indexOf('.')
        extension = name.substring(extensionIndex)
        nameWithoutExtension = name.substring(0, extensionIndex)
    }
}